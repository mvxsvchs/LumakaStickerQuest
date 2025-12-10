package com.example.lumaka.ui.feature.bingo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.R
import com.example.lumaka.data.local.BingoCellEntity
import com.example.lumaka.data.repository.BingoBoardRepository
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.data.remote.dto.BoardDto
import com.example.lumaka.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.WeekFields

private const val BINGO_COST = 10

data class BingoCell(
    val id: Int,
    val title: String,
    val unlocked: Boolean = false,
    val stickerResId: Int? = null
)

data class BingoUiState(
    val cells: List<BingoCell> = emptyList(),
    val lastSticker: String? = null,
    val message: String? = null,
    val weekKey: String = currentWeekKey()
) {
    val remainingLocked: Int get() = cells.count { !it.unlocked }
}

private data class Sticker(val name: String, val resId: Int, val id: Int)
@HiltViewModel
class BingoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository,
    private val bingoBoardRepository: BingoBoardRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val stickerPool: List<Sticker> = buildList {
        (1..29).forEach { idx ->
            StickerAssets.resIdFor(idx)?.let { res ->
                add(Sticker(name = "Red Panda $idx", resId = res, id = idx))
            }
        }
        (1..31).forEach { idx ->
            StickerAssets.resIdFor(100 + idx)?.let { res ->
                add(Sticker(name = "Skzoo $idx", resId = res, id = 100 + idx))
            }
        }
    }

    private val _uiState = MutableStateFlow(BingoUiState(cells = defaultCells()))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            UserSession.user
                .collectLatest { user ->
                loadBoard(user)
            }
        }
    }

    fun ensureWeekIsCurrent() {
        val currentKey = currentWeekKey()
        if (_uiState.value.weekKey != currentKey) {
            _uiState.update {
                BingoUiState(cells = defaultCells(), weekKey = currentKey)
            }
            viewModelScope.launch { persistState(currentUserEmail()) }
        }
    }

    fun purchaseRandomCell() {
        ensureWeekIsCurrent()
        val currentUser = UserSession.user.value ?: run {
            _uiState.update { it.copy(message = context.getString(R.string.bingo_message_login_required)) }
            return
        }
        val ownedStickerIds = currentUser.stickerid.toSet()
        val availableStickers = stickerPool.filterNot { it.id in ownedStickerIds }
        if (availableStickers.isEmpty()) {
            _uiState.update { it.copy(message = context.getString(R.string.bingo_message_all_collected)) }
            return
        }
        val lockedCells = _uiState.value.cells.filter { !it.unlocked }
        if (lockedCells.isEmpty()) {
            _uiState.update { it.copy(message = context.getString(R.string.bingo_message_all_unlocked)) }
            return
        }
        if (currentUser.points < BINGO_COST) {
            _uiState.update { it.copy(message = context.getString(R.string.bingo_message_not_enough_points)) }
            return
        }

        val sticker = availableStickers.random()
        val newPoints = (currentUser.points - BINGO_COST).coerceAtLeast(0)
        val updatedUser = currentUser.copy(
            points = newPoints,
            stickerid = currentUser.stickerid + sticker.id
        )
        UserSession.update(updatedUser)

        viewModelScope.launch {
            sessionRepository.saveUser(updatedUser)
            userRepository.updateStickers(updatedUser.userid, updatedUser.stickerid)
            bingoBoardRepository.fillRandomField(updatedUser.userid, sticker.id)
            val remoteBoard = runCatching { bingoBoardRepository.fetchRemoteBoard(updatedUser.userid) }.getOrNull()
            if (remoteBoard != null) {
                val remoteCells = cellsFromRemote(remoteBoard)
                if (remoteCells.isNotEmpty()) {
                    _uiState.update { current ->
                        current.copy(
                            cells = remoteCells,
                            weekKey = currentWeekKey()
                        )
                    }
                    persistState(updatedUser.email)
                }
            }
            val refreshed = runCatching { userRepository.getUserById(updatedUser.userid) }.getOrNull()
            if (refreshed != null) {
                UserSession.update(refreshed)
                sessionRepository.saveUser(refreshed)
            }
        }
    }

    private suspend fun loadBoard(user: User?) {
        val currentWeek = currentWeekKey()
        val email = user?.email?.takeIf { it.isNotBlank() }
        if (user == null) {
            _uiState.value = BingoUiState(cells = defaultCells(), weekKey = currentWeek)
            return
        }

        val remoteBoard = bingoBoardRepository.fetchRemoteBoard(user.userid)
        if (remoteBoard != null) {
            val remoteCells = cellsFromRemote(remoteBoard)
            _uiState.value = BingoUiState(
                cells = if (remoteCells.isNotEmpty()) remoteCells else defaultCells(),
                lastSticker = null,
                message = null,
                weekKey = currentWeek
            )
            persistState(email)
            return
        }

        if (email == null) {
            _uiState.value = BingoUiState(cells = defaultCells(), weekKey = currentWeek)
            return
        }

        val snapshot = bingoBoardRepository.loadBoard(email)
        val boardWeek = snapshot.board?.weekKey
        if (boardWeek == currentWeek) {
            val restoredCells = restoreCellsFromSnapshot(snapshot.cells)
            _uiState.value = BingoUiState(
                cells = restoredCells,
                lastSticker = snapshot.board?.lastSticker,
                message = null,
                weekKey = boardWeek
            )
        } else {
            _uiState.value = BingoUiState(cells = defaultCells(), weekKey = currentWeek)
            persistState(email)
        }
    }

    private fun restoreCellsFromSnapshot(cells: List<BingoCellEntity>): List<BingoCell> {
        if (cells.isEmpty()) return defaultCells()
        return cells.sortedBy { it.cellId }.mapIndexed { index, stored ->
            val title = context.getString(R.string.bingo_cell_title, index + 1)
            BingoCell(
                id = stored.cellId,
                title = title,
                unlocked = stored.unlocked,
                stickerResId = stored.stickerResId
            )
        }
    }

    private fun cellsFromRemote(board: BoardDto): List<BingoCell> {
        if (board.fields.isEmpty()) return emptyList()
        return board.fields
            .sortedBy { it.id }
            .mapIndexed { index, field ->
                val hasSticker = field.stickerId != null
                val stickerRes = field.stickerId?.let { StickerAssets.resIdFor(it) }
                val fallbackTitle = context.getString(R.string.bingo_cell_title, index + 1)
                val title = field.name?.takeIf { it.isNotBlank() } ?: fallbackTitle
                BingoCell(
                    id = field.id,
                    title = title,
                    unlocked = hasSticker,
                    stickerResId = stickerRes
                )
            }
    }

    private suspend fun persistState(userEmail: String?) {
        val email = userEmail?.takeIf { it.isNotBlank() } ?: return
        val state = _uiState.value
        val cellEntities = state.cells.map {
            BingoCellEntity(
                userEmail = email,
                cellId = it.id,
                weekKey = state.weekKey,
                unlocked = it.unlocked,
                stickerResId = it.stickerResId
            )
        }
        bingoBoardRepository.saveBoard(
            userEmail = email,
            weekKey = state.weekKey,
            lastSticker = state.lastSticker,
            cells = cellEntities
        )
    }

    private fun currentUserEmail(): String? = UserSession.user.value?.email

    private fun defaultCells(): List<BingoCell> =
        (0 until 9).map { index ->
            BingoCell(id = index, title = context.getString(R.string.bingo_cell_title, index + 1))
        }

}

private fun currentWeekKey(): String {
    val zone = ZoneId.of("Europe/Berlin")
    val now = ZonedDateTime.now(zone)
    val weekFields = WeekFields.ISO
    val week = now.get(weekFields.weekOfWeekBasedYear())
    val year = now.get(weekFields.weekBasedYear())
    return "$year-W$week"
}

package com.example.lumaka.ui.feature.bingo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.R
import com.example.lumaka.data.repository.BingoBoardRepository
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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
    private val pointsRepository: PointsRepository,
    private val sessionRepository: SessionRepository,
    private val bingoBoardRepository: BingoBoardRepository
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
                .map { it?.email?.lowercase().orEmpty() }
                .distinctUntilChanged()
                .collectLatest { email ->
                loadFromStorage(email.ifBlank { null })
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

        val chosen = lockedCells.random()
        val sticker = availableStickers.random()
        val updatedCells = _uiState.value.cells.map { cell ->
            if (cell.id == chosen.id) {
                cell.copy(unlocked = true, stickerResId = sticker.resId)
            } else cell
        }

        val bingoBonus = if (unlocksBingo(updatedCells)) 5 else 0
        val newPoints = (currentUser.points - BINGO_COST + bingoBonus).coerceAtLeast(0)
        val updatedUser = currentUser.copy(
            points = newPoints,
            stickerid = currentUser.stickerid + sticker.id
        )
        UserSession.update(updatedUser)
        _uiState.update { it.copy(cells = updatedCells, lastSticker = sticker.name, message = null) }
        viewModelScope.launch { persistState(updatedUser.email) }

        viewModelScope.launch {
            sessionRepository.saveUser(updatedUser)
            pointsRepository.setPoints(updatedUser.userid, newPoints)
        }
    }

    private fun baseCells(): List<BingoCell> = defaultCells()

    private suspend fun loadFromStorage(userEmail: String?) {
        val currentWeek = currentWeekKey()
        val email = userEmail?.takeIf { it.isNotBlank() }
        if (email == null) {
            _uiState.value = BingoUiState(cells = defaultCells(), weekKey = currentWeek)
            return
        }
        val snapshot = bingoBoardRepository.loadBoard(email)
        val boardWeek = snapshot.board?.weekKey
        if (boardWeek == currentWeek) {
            val restoredCells = baseCells().map { cell ->
                val stored = snapshot.cells.firstOrNull { it.cellId == cell.id }
                if (stored != null) {
                    cell.copy(unlocked = stored.unlocked, stickerResId = stored.stickerResId)
                } else cell
            }
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

    private suspend fun persistState(userEmail: String?) {
        val email = userEmail?.takeIf { it.isNotBlank() } ?: return
        val state = _uiState.value
        val cellEntities = state.cells.map {
            com.example.lumaka.data.local.BingoCellEntity(
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

    private fun unlocksBingo(cells: List<BingoCell>): Boolean {
        val grid = cells.sortedBy { it.id }.map { it.unlocked }
        fun row(idx: Int) = grid[idx] && grid[idx + 1] && grid[idx + 2]
        fun col(idx: Int) = grid[idx] && grid[idx + 3] && grid[idx + 6]
        fun diag1() = grid[0] && grid[4] && grid[8]
        fun diag2() = grid[2] && grid[4] && grid[6]
        return row(0) || row(3) || row(6) || col(0) || col(1) || col(2) || diag1() || diag2()
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

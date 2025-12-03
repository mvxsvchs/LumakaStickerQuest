package com.example.lumaka.ui.feature.bingo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.R
import com.example.lumaka.ui.feature.bingo.StickerAssets
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val cells: List<BingoCell> = (0 until 9).map { index ->
        BingoCell(id = index, title = "Feld ${index + 1}")
    },
    val lastSticker: String? = null,
    val message: String? = null,
    val weekKey: String = currentWeekKey()
) {
    val remainingLocked: Int get() = cells.count { !it.unlocked }
}

private data class Sticker(val name: String, val resId: Int, val id: Int)
@HiltViewModel
class BingoViewModel @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val sessionRepository: SessionRepository
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

    private val _uiState = MutableStateFlow(BingoUiState())
    val uiState = _uiState.asStateFlow()

    fun ensureWeekIsCurrent() {
        val currentKey = currentWeekKey()
        if (_uiState.value.weekKey != currentKey) {
            _uiState.update {
                BingoUiState(weekKey = currentKey)
            }
        }
    }

    fun purchaseRandomCell() {
        ensureWeekIsCurrent()
        val currentUser = UserSession.user.value ?: run {
            _uiState.update { it.copy(message = "Bitte einloggen, um zu spielen.") }
            return
        }
        val ownedStickerIds = currentUser.stickerid.toSet()
        val availableStickers = stickerPool.filterNot { it.id in ownedStickerIds }
        if (availableStickers.isEmpty()) {
            _uiState.update { it.copy(message = "Alle Sticker bereits gesammelt.") }
            return
        }
        val lockedCells = _uiState.value.cells.filter { !it.unlocked }
        if (lockedCells.isEmpty()) {
            _uiState.update { it.copy(message = "Alle Felder sind bereits freigeschaltet.") }
            return
        }
        if (currentUser.points < BINGO_COST) {
            _uiState.update { it.copy(message = "Nicht genug Punkte (10 noetig).") }
            return
        }

        val chosen = lockedCells.random()
        val sticker = availableStickers.random()
        val updatedCells = _uiState.value.cells.map { cell ->
            if (cell.id == chosen.id) {
                cell.copy(unlocked = true, stickerResId = sticker.resId)
            } else cell
        }

        val newPoints = (currentUser.points - BINGO_COST).coerceAtLeast(0)
        val updatedUser = currentUser.copy(
            points = newPoints,
            stickerid = currentUser.stickerid + sticker.id
        )
        UserSession.update(updatedUser)
        _uiState.update { it.copy(cells = updatedCells, lastSticker = sticker.name, message = null) }

        viewModelScope.launch {
            sessionRepository.saveUser(updatedUser)
            pointsRepository.setPoints(updatedUser.email, newPoints)
        }
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

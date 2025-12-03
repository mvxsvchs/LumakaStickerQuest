package com.example.lumaka.ui.feature.bingo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val BINGO_COST = 10

data class BingoCell(
    val id: Int,
    val title: String,
    val unlocked: Boolean = false,
    val stickerName: String? = null,
    val stickerResId: Int? = null
)

data class BingoUiState(
    val cells: List<BingoCell> = (0 until 9).map { index ->
        BingoCell(id = index, title = "Feld ${index + 1}")
    },
    val lastSticker: String? = null,
    val message: String? = null
) {
    val remainingLocked: Int get() = cells.count { !it.unlocked }
}

private data class Sticker(val name: String, val resId: Int, val id: Int)

@HiltViewModel
class BingoViewModel @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val stickerPool = listOf(
        Sticker("Red Panda 1", R.drawable.sticker_redpanda_01, id = 1),
        Sticker("Red Panda 2", R.drawable.sticker_redpanda_02, id = 2),
        Sticker("Red Panda 3", R.drawable.sticker_redpanda_03, id = 3),
        Sticker("Skzoo 1", R.drawable.sticker_skzoo_01, id = 101),
        Sticker("Skzoo 2", R.drawable.sticker_skzoo_02, id = 102),
        Sticker("Skzoo 3", R.drawable.sticker_skzoo_03, id = 103),
    )

    private val _uiState = MutableStateFlow(BingoUiState())
    val uiState = _uiState.asStateFlow()

    fun purchaseRandomCell() {
        val currentUser = UserSession.user.value ?: run {
            _uiState.update { it.copy(message = "Bitte einloggen, um zu spielen.") }
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
        val sticker = stickerPool.random()
        val updatedCells = _uiState.value.cells.map { cell ->
            if (cell.id == chosen.id) {
                cell.copy(unlocked = true, stickerName = sticker.name, stickerResId = sticker.resId)
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

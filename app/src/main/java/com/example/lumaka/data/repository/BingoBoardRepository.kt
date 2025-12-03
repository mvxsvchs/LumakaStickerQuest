package com.example.lumaka.data.repository

import com.example.lumaka.data.local.BingoBoardDao
import com.example.lumaka.data.local.BingoBoardEntity
import com.example.lumaka.data.local.BingoCellEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BingoBoardRepository @Inject constructor(
    private val dao: BingoBoardDao
) {

    data class BoardSnapshot(
        val board: BingoBoardEntity?,
        val cells: List<BingoCellEntity>
    )

    suspend fun loadBoard(userEmail: String): BoardSnapshot {
        if (userEmail.isBlank()) return BoardSnapshot(board = null, cells = emptyList())
        val normalizedEmail = userEmail.lowercase()
        val board = dao.getBoard(normalizedEmail)
        val cells = board?.weekKey?.let { dao.getCellsForWeek(normalizedEmail, it) } ?: emptyList()
        return BoardSnapshot(board = board, cells = cells)
    }

    suspend fun saveBoard(userEmail: String, weekKey: String, lastSticker: String?, cells: List<BingoCellEntity>) {
        if (userEmail.isBlank()) return
        val normalizedEmail = userEmail.lowercase()
        dao.upsertBoard(BingoBoardEntity(userEmail = normalizedEmail, weekKey = weekKey, lastSticker = lastSticker))
        dao.deleteCellsNotInWeek(normalizedEmail, weekKey)
        dao.upsertCells(cells.map { it.copy(userEmail = normalizedEmail, weekKey = weekKey) })
    }
}

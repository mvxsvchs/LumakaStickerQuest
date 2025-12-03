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

    suspend fun loadBoard(): BoardSnapshot {
        val board = dao.getBoard()
        val cells = board?.weekKey?.let { dao.getCellsForWeek(it) } ?: emptyList()
        return BoardSnapshot(board = board, cells = cells)
    }

    suspend fun saveBoard(weekKey: String, lastSticker: String?, cells: List<BingoCellEntity>) {
        dao.upsertBoard(BingoBoardEntity(weekKey = weekKey, lastSticker = lastSticker, id = 0))
        dao.deleteCellsNotInWeek(weekKey)
        dao.upsertCells(cells.map { it.copy(weekKey = weekKey) })
    }
}

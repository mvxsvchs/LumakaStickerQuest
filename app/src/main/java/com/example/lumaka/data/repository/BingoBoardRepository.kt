package com.example.lumaka.data.repository

import android.util.Log
import com.example.lumaka.data.local.BingoBoardDao
import com.example.lumaka.data.local.BingoBoardEntity
import com.example.lumaka.data.local.BingoCellEntity
import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.data.remote.dto.BoardDto
import com.example.lumaka.data.remote.dto.StickerIdRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BingoBoardRepository @Inject constructor(
    private val dao: BingoBoardDao,
    private val api: QuestService
) {

    data class BoardSnapshot(
        val board: BingoBoardEntity?,
        val cells: List<BingoCellEntity>
    )

    suspend fun fetchRemoteBoard(userId: Int): BoardDto? {
        if (userId <= 0) return null
        return try {
            api.getBoard(userId = userId)
        } catch (t: Throwable) {
            if (t is retrofit2.HttpException && t.code() == 404) {
                null
            } else {
                Log.w("BingoBoardRepository", "Failed to fetch remote board: ${t.message}", t)
                null
            }
        }
    }

    suspend fun fillRandomField(userId: Int, stickerId: Int) {
        if (userId <= 0 || stickerId <= 0) return
        try {
            api.fillRandomField(userId = userId, request = StickerIdRequest(stickerId))
        } catch (t: Throwable) {
            Log.w("BingoBoardRepository", "Failed to fill random field: ${t.message}", t)
        }
    }

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

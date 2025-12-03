package com.example.lumaka.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BingoBoardDao {
    @Query("SELECT * FROM bingo_board WHERE id = 0")
    suspend fun getBoard(): BingoBoardEntity?

    @Query("SELECT * FROM bingo_cells WHERE weekKey = :weekKey ORDER BY cellId")
    suspend fun getCellsForWeek(weekKey: String): List<BingoCellEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBoard(board: BingoBoardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCells(cells: List<BingoCellEntity>)

    @Query("DELETE FROM bingo_cells WHERE weekKey != :weekKey")
    suspend fun deleteCellsNotInWeek(weekKey: String)

    @Query("DELETE FROM bingo_cells")
    suspend fun clearCells()
}

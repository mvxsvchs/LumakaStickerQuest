package com.example.lumaka.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BingoBoardDao {
    @Query("SELECT * FROM bingo_board WHERE userEmail = :userEmail")
    suspend fun getBoard(userEmail: String): BingoBoardEntity?

    @Query("SELECT * FROM bingo_cells WHERE userEmail = :userEmail AND weekKey = :weekKey ORDER BY cellId")
    suspend fun getCellsForWeek(userEmail: String, weekKey: String): List<BingoCellEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBoard(board: BingoBoardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCells(cells: List<BingoCellEntity>)

    @Query("DELETE FROM bingo_cells WHERE userEmail = :userEmail AND weekKey != :weekKey")
    suspend fun deleteCellsNotInWeek(userEmail: String, weekKey: String)

    @Query("DELETE FROM bingo_cells WHERE userEmail = :userEmail")
    suspend fun clearCells(userEmail: String)
}

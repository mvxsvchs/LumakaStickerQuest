package com.example.lumaka.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bingo_cells")
data class BingoCellEntity(
    @PrimaryKey val cellId: Int,
    val weekKey: String,
    val unlocked: Boolean,
    val stickerResId: Int?
)

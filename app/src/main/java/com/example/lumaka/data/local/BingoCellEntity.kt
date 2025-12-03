package com.example.lumaka.data.local

import androidx.room.Entity

@Entity(tableName = "bingo_cells", primaryKeys = ["userEmail", "weekKey", "cellId"])
data class BingoCellEntity(
    val userEmail: String,
    val weekKey: String,
    val cellId: Int,
    val unlocked: Boolean,
    val stickerResId: Int?
)

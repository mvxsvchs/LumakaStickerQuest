package com.example.lumaka.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bingo_board")
data class BingoBoardEntity(
    @PrimaryKey val userEmail: String,
    val weekKey: String,
    val lastSticker: String?
)

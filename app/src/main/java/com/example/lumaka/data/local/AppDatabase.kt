package com.example.lumaka.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BingoBoardEntity::class,
        BingoCellEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bingoBoardDao(): BingoBoardDao
}

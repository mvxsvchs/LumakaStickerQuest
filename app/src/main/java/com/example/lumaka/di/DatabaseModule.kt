package com.example.lumaka.di

import android.content.Context
import androidx.room.Room
import com.example.lumaka.data.local.AppDatabase
import com.example.lumaka.data.local.BingoBoardDao
import com.example.lumaka.data.repository.BingoBoardRepository
import com.example.lumaka.data.remote.api.QuestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "lumaka.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideBingoBoardDao(db: AppDatabase): BingoBoardDao = db.bingoBoardDao()

    @Provides
    @Singleton
    fun provideBingoBoardRepository(
        dao: BingoBoardDao,
        api: QuestService
    ): BingoBoardRepository = BingoBoardRepository(dao, api)
}

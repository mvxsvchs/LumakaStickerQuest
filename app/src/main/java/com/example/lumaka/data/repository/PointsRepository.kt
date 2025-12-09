package com.example.lumaka.data.repository

import android.content.Context
import android.util.Log
import com.example.lumaka.data.remote.api.QuestService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import retrofit2.HttpException

@Singleton
class PointsRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val api: QuestService
) {
    private val logTag = "PointsRepository"
    private val prefs = context.getSharedPreferences("points_prefs", Context.MODE_PRIVATE)

    fun getPoints(userId: Int): Int {
        if (userId <= 0) return 0
        return prefs.getInt(key(userId), 0)
    }

    private fun key(userId: Int) = "points_$userId"
}

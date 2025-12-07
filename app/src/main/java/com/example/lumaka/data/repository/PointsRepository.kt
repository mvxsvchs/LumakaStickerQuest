package com.example.lumaka.data.repository

import android.content.Context
import android.util.Log
import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.data.remote.dto.PointsDTO
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

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

    suspend fun setPoints(userId: Int, points: Int) {
        if (userId <= 0) return
        prefs.edit().putInt(key(userId), points.coerceAtLeast(0)).apply()
        val payload = PointsDTO(userId = userId, points = points.coerceAtLeast(0))
        repeat(3) { attempt ->
            try {
                api.updatePoints(payload)
                return
            } catch (t: Throwable) {
                Log.w(logTag, "Failed to sync points (attempt ${attempt + 1}): ${t.message}", t)
                if (attempt < 2) {
                    delay(500L * (attempt + 1))
                }
            }
        }
    }

    private fun key(userId: Int) = "points_$userId"
}

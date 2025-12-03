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

    fun getPoints(email: String): Int {
        if (email.isBlank()) return 0
        return prefs.getInt(key(email), 0)
    }

    suspend fun setPoints(email: String, points: Int) {
        if (email.isBlank()) return
        prefs.edit().putInt(key(email), points.coerceAtLeast(0)).apply()
        val payload = PointsDTO(email = email, points = points.coerceAtLeast(0))
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

    private fun key(email: String) = "points_${email.lowercase()}"
}

package com.example.lumaka.data.repository

import android.content.Context
import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.data.remote.dto.PointsDTO
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointsRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val api: QuestService
) {
    private val prefs = context.getSharedPreferences("points_prefs", Context.MODE_PRIVATE)

    fun getPoints(email: String): Int {
        if (email.isBlank()) return 0
        return prefs.getInt(key(email), 0)
    }

    suspend fun setPoints(email: String, points: Int) {
        if (email.isBlank()) return
        prefs.edit().putInt(key(email), points.coerceAtLeast(0)).apply()
        try {
            api.updatePoints(PointsDTO(email = email, points = points.coerceAtLeast(0)))
        } catch (_: Throwable) {
            // Swallow network errors; local cache still updated
        }
    }

    private fun key(email: String) = "points_${email.lowercase()}"
}

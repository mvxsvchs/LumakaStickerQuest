package com.example.lumaka.data.repository

import android.content.Context
import com.example.lumaka.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User?) {
        if (user == null) {
            prefs.edit().clear().apply()
            return
        }
        val stickerString = user.stickerid.joinToString(separator = ",")
        prefs.edit()
            .putString("username", user.username)
            .putString("email", user.email)
            .putInt("userid", user.userid)
            .putInt("points", user.points)
            .putString("sticker_ids", stickerString)
            .apply()
    }

    fun loadUser(): User? {
        val email = prefs.getString("email", null) ?: return null
        val username = prefs.getString("username", "") ?: ""
        val userid = prefs.getInt("userid", 0)
        val points = prefs.getInt("points", 0)
        val stickerString = prefs.getString("sticker_ids", "") ?: ""
        val stickerIds = stickerString
            .split(",")
            .mapNotNull { it.toIntOrNull() }
            .filter { it > 0 }
        return User(
            username = username,
            userid = userid,
            points = points,
            stickerid = stickerIds,
            email = email
        )
    }
}

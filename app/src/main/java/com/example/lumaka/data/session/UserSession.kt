package com.example.lumaka.data.session

import com.example.lumaka.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSession {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    fun update(user: User?) {
        _user.value = user
    }

    fun addPoints(delta: Int) {
        _user.value = _user.value?.let { current ->
            val newPoints = (current.points + delta).coerceAtLeast(0)
            current.copy(points = newPoints)
        }
    }

    fun updateAvatar(avatarId: Int) {
        _user.value = _user.value?.copy(avatarId = avatarId)
    }
}

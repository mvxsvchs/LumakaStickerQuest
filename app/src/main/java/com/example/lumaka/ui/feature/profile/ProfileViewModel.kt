package com.example.lumaka.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    val user: StateFlow<User?> = UserSession.user
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun selectAvatar(avatarId: Int) {
        UserSession.updateAvatar(avatarId)
    }

    fun refreshUser() {
        val current = UserSession.user.value ?: return
        if (current.userid <= 0) return
        viewModelScope.launch {
            val remote = runCatching { userRepository.getUserById(current.userid) }.getOrNull()
            if (remote != null) {
                UserSession.update(remote)
                sessionRepository.saveUser(remote)
            }
        }
    }
}

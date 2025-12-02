package com.example.lumaka.ui.feature.settings

import androidx.lifecycle.ViewModel
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _logoutRequested = MutableStateFlow(false)
    val logoutRequested = _logoutRequested.asStateFlow()

    fun logout() {
        UserSession.update(null)
        sessionRepository.saveUser(null)
        _logoutRequested.update { true }
    }
}

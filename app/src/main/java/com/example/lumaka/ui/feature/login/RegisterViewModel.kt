package com.example.lumaka.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.R
import com.example.lumaka.data.repository.ApiResult
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.domain.model.Registration
import com.example.lumaka.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val pointsRepository: PointsRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    enum class RegisterError { PASSWORD_MISMATCH, REQUIRED_FIELDS, GENERIC }

    data class RegisterUiState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val error: RegisterError? = null,
        val errorMessageId: Int? = null,
    )

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onRegister(username: String, email: String, password: String, confirmPassword: String) {
        val trimmedUsername = username.trim()
        val trimmedEmail = email.trim()
        if (password != confirmPassword) {
            _uiState.update { it.copy(error = RegisterError.PASSWORD_MISMATCH, errorMessageId = null) }
            return
        }
        if (trimmedUsername.isBlank() || trimmedEmail.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(error = RegisterError.REQUIRED_FIELDS, errorMessageId = null) }
            return
        }
        viewModelScope.launch {
            _uiState.update { RegisterUiState(isLoading = true) }
            val registerData = Registration(username = trimmedUsername, mail = trimmedEmail, password = password)
            val result = userRepository.registerUser(registration = registerData)
            _uiState.update {
                when (result) {
                    is ApiResult.Success -> {
                        val user = User(
                            username = trimmedUsername,
                            userid = 0,
                            points = 0,
                            stickerid = emptyList(),
                            email = trimmedEmail,
                        )
                        UserSession.update(user)
                        sessionRepository.saveUser(user)
                        RegisterUiState(isSuccess = true)
                    }
                    is ApiResult.Error -> {
                        val messageId = when {
                            result.isNetworkError -> R.string.register_error_network
                            result.statusCode == 409 -> R.string.register_error_conflict
                            else -> R.string.register_error_generic
                        }
                        RegisterUiState(error = RegisterError.GENERIC, errorMessageId = messageId)
                    }
                }
            }
        }
    }
}

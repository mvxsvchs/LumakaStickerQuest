package com.example.lumaka.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.domain.model.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.lumaka.data.session.UserSession

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    data class LoginUiState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessageId: Int? = null,
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            val trimmedEmail = email.trim()
            val loweredEmail = trimmedEmail.lowercase()

            // Try with the original casing first, then fallback to lowercase to make login case-insensitive.
            val primaryResult = userRepository.loginUser(login = Login(mail = trimmedEmail, password = password))
            val loginResult = primaryResult ?: if (trimmedEmail != loweredEmail) {
                userRepository.loginUser(login = Login(mail = loweredEmail, password = password))
            } else null
            _uiState.value = when {
                loginResult != null -> {
                    UserSession.update(loginResult)
                    LoginUiState(isSuccess = true)
                }
                else -> LoginUiState(errorMessageId = com.example.lumaka.R.string.login_error_invalid)
            }
        }
    }
}

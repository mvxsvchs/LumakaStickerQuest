package com.example.lumaka.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.R
import com.example.lumaka.data.repository.ApiResult
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.domain.model.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val pointsRepository: PointsRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    data class LoginUiState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val errorMessageId: Int? = null,
        val isNetworkError: Boolean = false,
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            val trimmedEmail = email.trim()
            val loweredEmail = trimmedEmail.lowercase()

            val primaryResult = userRepository.loginUser(login = Login(mail = trimmedEmail, password = password))
            val finalResult = if (primaryResult is ApiResult.Error && trimmedEmail != loweredEmail) {
                userRepository.loginUser(login = Login(mail = loweredEmail, password = password))
            } else primaryResult

            _uiState.value = when (finalResult) {
                is ApiResult.Success -> {
                    val loginResult = finalResult.data
                    val effectiveEmail = loginResult.email.ifBlank { loweredEmail }
                    val storedPoints = pointsRepository.getPoints(loginResult.userid)
                    val mergedPoints = max(loginResult.points, storedPoints)
                    val userWithPoints = loginResult.copy(points = mergedPoints, email = effectiveEmail)
                    UserSession.update(userWithPoints)
                    sessionRepository.saveUser(userWithPoints)
                    LoginUiState(isSuccess = true)
                }
                is ApiResult.Error -> {
                    val messageId = when {
                        finalResult.isNetworkError -> R.string.login_error_network
                        finalResult.statusCode == 400 -> R.string.login_error_bad_request
                        finalResult.statusCode == 401 -> R.string.login_error_invalid
                        else -> R.string.login_error_generic
                    }
                    LoginUiState(errorMessageId = messageId, isNetworkError = finalResult.isNetworkError)
                }
            }
        }
    }
}

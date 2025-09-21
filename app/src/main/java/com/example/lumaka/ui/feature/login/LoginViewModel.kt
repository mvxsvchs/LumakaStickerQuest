package com.example.lumaka.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.domain.model.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _onLoginResult = MutableSharedFlow<Boolean>()
    val onLoginResult = _onLoginResult.asSharedFlow()
    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            val loginData = Login(mail = email, password = password)
            val loginResult = userRepository.loginUser(login = loginData)
            _onLoginResult.emit(value = loginResult)
        }
    }
}
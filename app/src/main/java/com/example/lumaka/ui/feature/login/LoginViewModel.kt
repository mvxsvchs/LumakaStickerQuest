package com.example.lumaka.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.domain.model.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            val loginData = Login(mail = email, password = password)
            userRepository.loginUser(login = loginData)
        }
    }
}
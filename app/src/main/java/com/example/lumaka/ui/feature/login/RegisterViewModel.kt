package com.example.lumaka.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.UserRepository
import com.example.lumaka.domain.model.Registration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    fun onRegister(username: String, email: String, password: String) {
        viewModelScope.launch {
            val registerData = Registration(username = username, mail = email, password = password)
            userRepository.registerUser(registration = registerData)
        }
    }
}
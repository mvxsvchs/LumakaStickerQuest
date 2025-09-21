package com.example.lumaka.data.repository

import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.data.remote.dto.RegisterDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val api: QuestService) {
    suspend fun registerUser(username: String, mail: String, password: String) {
        api.registerUser(register = RegisterDTO(username, mail, password))
    }
}
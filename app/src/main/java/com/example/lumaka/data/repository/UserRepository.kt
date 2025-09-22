package com.example.lumaka.data.repository

import com.example.lumaka.data.mapper.toDTO
import com.example.lumaka.data.mapper.toDomain
import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.domain.model.Login
import com.example.lumaka.domain.model.Registration
import com.example.lumaka.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val api: QuestService) {
    suspend fun registerUser(registration: Registration) {
        api.registerUser(register = registration.toDTO())
    }

    suspend fun loginUser(login: Login): Boolean {
        val result = api.loginUser(login = login.toDTO())
        return result != null
    }

    suspend fun getUserById(userId: Int): User? {
        val result = api.getUserById(userid = userId)
        return result?.toDomain()
    }
}
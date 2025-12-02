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
    suspend fun registerUser(registration: Registration): Boolean {
        return try {
            api.registerUser(register = registration.toDTO())
            true
        } catch (t: Throwable) {
            false
        }
    }

    suspend fun loginUser(login: Login): User? {
        return try {
            api.loginUser(login = login.toDTO())?.toDomain(fallbackEmail = login.mail)
        } catch (t: Throwable) {
            null
        }
    }

    suspend fun getUserById(userId: Int): User? {
        val result = api.getUserById(userid = userId)
        return result?.toDomain()
    }
}

package com.example.lumaka.data.repository

import android.util.Log
import com.example.lumaka.data.mapper.toDTO
import com.example.lumaka.data.mapper.toDomain
import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.domain.model.Login
import com.example.lumaka.domain.model.Registration
import com.example.lumaka.domain.model.User
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException

@Singleton
class UserRepository @Inject constructor(private val api: QuestService) {
    suspend fun registerUser(registration: Registration): ApiResult<Unit> {
        return try {
            api.registerUser(register = registration.toDTO())
            ApiResult.Success(Unit)
        } catch (e: HttpException) {
            ApiResult.Error(message = e.safeMessage(), statusCode = e.code())
        } catch (e: IOException) {
            ApiResult.Error(message = e.safeMessage(), isNetworkError = true)
        } catch (t: Throwable) {
            ApiResult.Error(message = t.safeMessage())
        }
    }

    suspend fun loginUser(login: Login): ApiResult<User> {
        return try {
            val user = api.loginUser(login = login.toDTO())?.toDomain(fallbackEmail = login.mail)
            if (user != null) {
                ApiResult.Success(user)
            } else {
                ApiResult.Error(message = "Ungültige Anmeldedaten", statusCode = 401)
            }
        } catch (e: HttpException) {
            ApiResult.Error(message = e.safeMessage(), statusCode = e.code())
        } catch (e: IOException) {
            ApiResult.Error(message = e.safeMessage(), isNetworkError = true)
        } catch (t: Throwable) {
            ApiResult.Error(message = t.safeMessage())
        }
    }

    suspend fun getUserById(userId: Int): User? {
        val result = api.getUserById(userid = userId)
        return result?.toDomain()
    }

    suspend fun updateStickers(userId: Int, stickers: List<Int>) {
        if (userId <= 0) return
        try {
            api.updateStickers(userId = userId, request = com.example.lumaka.data.remote.dto.UpdateStickersRequest(stickers))
        } catch (e: Throwable) {
            Log.e("UserRepository", "Failed to update stickers: ${e.message}", e)
        }
    }

    private fun Throwable.safeMessage(): String =
        this.localizedMessage ?: this.message ?: this::class.java.simpleName
}

package com.example.lumaka.data.remote.api

import com.example.lumaka.data.remote.dto.LoginDTO
import com.example.lumaka.data.remote.dto.RegisterDTO
import com.example.lumaka.data.remote.dto.UserDTO
import com.example.lumaka.data.remote.dto.PointsDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QuestService {
    @POST(value = "user/register")
    suspend fun registerUser(@Body register: RegisterDTO)

    @POST(value = "user/login")
    suspend fun loginUser(@Body login: LoginDTO): UserDTO?

    @GET(value = "user/{id}")
    suspend fun getUserById(@Path(value = "id") userid: Int): UserDTO?

    @POST(value = "user/points")
    suspend fun updatePoints(@Body pointsDTO: PointsDTO)
}

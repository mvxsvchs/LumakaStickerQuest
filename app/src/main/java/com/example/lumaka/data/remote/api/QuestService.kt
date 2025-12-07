package com.example.lumaka.data.remote.api

import com.example.lumaka.data.remote.dto.LoginDTO
import com.example.lumaka.data.remote.dto.RegisterDTO
import com.example.lumaka.data.remote.dto.UserDTO
import com.example.lumaka.data.remote.dto.PointsDTO
import com.example.lumaka.data.remote.dto.TaskCreateRequest
import com.example.lumaka.data.remote.dto.TaskCreateResponse
import com.example.lumaka.data.remote.dto.TaskResponse
import com.example.lumaka.data.remote.dto.TaskUpdateRequest
import com.example.lumaka.data.remote.dto.TaskUpdateResponse
import com.example.lumaka.data.remote.dto.UpdateStickersRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST(value = "task")
    suspend fun createTask(@Body request: TaskCreateRequest): TaskCreateResponse

    @GET(value = "task/{userId}")
    suspend fun getTasks(@Path(value = "userId") userId: Int): List<TaskResponse>

    @DELETE(value = "task/{id}")
    suspend fun deleteTask(@Path(value = "id") taskId: Int)

    @PUT(value = "task/{id}")
    suspend fun updateTaskCompletion(
        @Path(value = "id") taskId: Int,
        @Body request: TaskUpdateRequest
    ): TaskUpdateResponse

    @PUT(value = "user/{id}/stickers")
    suspend fun updateStickers(
        @Path(value = "id") userId: Int,
        @Body request: UpdateStickersRequest
    )
}

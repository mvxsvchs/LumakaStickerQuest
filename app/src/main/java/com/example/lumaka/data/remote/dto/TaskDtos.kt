package com.example.lumaka.data.remote.dto

import com.squareup.moshi.Json

data class TaskCreateRequest(
    val userId: Int,
    val taskDescription: String,
    val categoryId: Int
)

data class TaskCreateResponse(
    @Json(name = "task_id") val taskId: Int
)

data class TaskUpdateRequest(
    val isCompleted: Boolean
)

data class TaskUpdateResponse(
    @Json(name = "user_points") val userPoints: Int
)

data class TaskResponse(
    val taskId: Int,
    val taskDescription: String,
    val categoryId: Int,
    val isCompleted: Boolean,
    val position: Int,
    val pointsReward: Int
)

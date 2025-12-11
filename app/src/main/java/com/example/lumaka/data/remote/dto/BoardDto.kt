package com.example.lumaka.data.remote.dto

import com.squareup.moshi.Json

data class BoardDto(
    val id: Int,
    val userId: Int,
    val isCompleted: Boolean,
    val createdAt: String,
    val fields: List<BoardFieldDto>
)

data class BoardFieldDto(
    val id: Int,
    val name: String?,
    val stickerId: Int?
)

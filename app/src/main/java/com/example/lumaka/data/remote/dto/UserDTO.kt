package com.example.lumaka.data.remote.dto

data class UserDTO(
    val username: String,
    val userId: Int,
    val points: Int,
    val stickerId: List<Int>,
)
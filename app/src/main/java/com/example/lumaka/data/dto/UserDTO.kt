package com.example.lumaka.data.dto

data class UserDTO(
    val username: String,
    val userid: Int,
    val points: Int,
    val stickerid: List<Int>,
)
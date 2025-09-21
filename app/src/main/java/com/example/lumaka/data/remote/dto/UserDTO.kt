package com.example.lumaka.data.remote.dto

data class UserDTO(
    val username: String,
    val userid: Int,
    val points: Int,
    val stickerid: List<Int>,
)
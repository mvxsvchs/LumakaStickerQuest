package com.example.lumaka.domain.model

data class User(
    val username: String,
    val userid: Int,
    val points: Int,
    val stickerid: List<Int>,
    val email: String = "",
)

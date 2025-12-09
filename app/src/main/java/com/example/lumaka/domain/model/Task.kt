package com.example.lumaka.domain.model

data class Task(
    val id: Int,
    val title: String,
    val category: CategoryEnum,
    val completed: Boolean = false,
    val position: Int = 0,
    val pointsReward: Int = 0,
)

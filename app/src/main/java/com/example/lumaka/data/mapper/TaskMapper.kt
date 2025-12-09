package com.example.lumaka.data.mapper

import com.example.lumaka.data.remote.dto.TaskResponse
import com.example.lumaka.domain.model.CategoryEnum
import com.example.lumaka.domain.model.Task

fun TaskResponse.toDomain(): Task {
    val category = CategoryEnum.entries.firstOrNull { it.id == categoryId } ?: CategoryEnum.ALL
    return Task(
        id = taskId,
        title = taskDescription,
        category = category,
        completed = isCompleted,
        position = position,
        pointsReward = pointsReward
    )
}

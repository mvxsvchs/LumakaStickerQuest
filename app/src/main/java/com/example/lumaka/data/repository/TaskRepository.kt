package com.example.lumaka.data.repository

import android.util.Log
import com.example.lumaka.data.mapper.toDomain
import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.data.remote.dto.TaskCreateRequest
import com.example.lumaka.data.remote.dto.TaskUpdateRequest
import com.example.lumaka.domain.model.CategoryEnum
import com.example.lumaka.domain.model.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val api: QuestService
){
    private val logTag = "TaskRepository"

    suspend fun fetchTasks(userId: Int): List<Task> {
        if (userId <= 0) return emptyList()
        return try {
            api.getTasks(userId = userId)
                .map { it.toDomain() }
                .sortedBy { it.position }
        } catch (t: Throwable) {
            Log.w(logTag, "Failed to fetch tasks: ${t.message}", t)
            emptyList()
        }
    }

    suspend fun addTask(userId: Int, description: String, category: CategoryEnum): List<Task> {
        if (userId <= 0) return emptyList()
        return try {
            api.createTask(
                TaskCreateRequest(
                    userId = userId,
                    taskDescription = description,
                    categoryId = category.id
                )
            )
            fetchTasks(userId)
        } catch (t: Throwable) {
            Log.w(logTag, "Failed to add task: ${t.message}", t)
            fetchTasks(userId)
        }
    }

    suspend fun deleteTask(userId: Int, taskId: Int): List<Task> {
        if (userId <= 0 || taskId <= 0) return emptyList()
        return try {
            api.deleteTask(taskId)
            fetchTasks(userId)
        } catch (t: Throwable) {
            Log.w(logTag, "Failed to delete task $taskId: ${t.message}", t)
            fetchTasks(userId)
        }
    }

    suspend fun updateCompletion(userId: Int, taskId: Int, isCompleted: Boolean): Int? {
        if (userId <= 0 || taskId <= 0) return null
        return try {
            val response = api.updateTaskCompletion(
                taskId = taskId,
                request = TaskUpdateRequest(isCompleted = isCompleted)
            )
            response.userPoints
        } catch (t: Throwable) {
            Log.w(logTag, "Failed to toggle completion for $taskId: ${t.message}", t)
            null
        }
    }
}

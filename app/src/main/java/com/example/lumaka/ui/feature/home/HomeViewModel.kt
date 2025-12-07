package com.example.lumaka.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.PointsRepository
import com.example.lumaka.data.repository.TaskRepository
import com.example.lumaka.data.session.UserSession
import com.example.lumaka.data.repository.SessionRepository
import com.example.lumaka.domain.model.CategoryEnum
import com.example.lumaka.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import android.util.Log

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val logTag = "HomeViewModel"
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        viewModelScope.launch {
            UserSession.user.collectLatest { user ->
                if (user != null) {
                    loadTasks(user.userid)
                } else {
                    _tasks.value = emptyList()
                }
            }
        }
    }

    fun refreshTasks() {
        val userId = UserSession.user.value?.userid ?: return
        viewModelScope.launch { loadTasks(userId) }
    }

    private suspend fun loadTasks(userId: Int) {
        val fetched = taskRepository.fetchTasks(userId)
        _tasks.value = fetched
    }

    fun addTask(title: String, category: CategoryEnum) {
        val trimmed = title.trim()
        if (trimmed.isBlank()) return
        val user = UserSession.user.value ?: return
        val backendCategory = if (category == CategoryEnum.ALL) CategoryEnum.GENERAL else category
        viewModelScope.launch {
            val updated = taskRepository.addTask(
                userId = user.userid,
                description = trimmed,
                category = backendCategory
            )
            _tasks.value = updated
        }
    }

    fun toggleTask(id: Int) {
        val taskBefore = _tasks.value.firstOrNull { it.id == id } ?: return
        val user = UserSession.user.value ?: return
        val newCompleted = !taskBefore.completed
        _tasks.update { current ->
            current.map { task ->
                if (task.id == id) task.copy(completed = newCompleted) else task
            }
        }
        viewModelScope.launch {
            val newPoints = taskRepository.updateCompletion(
                userId = user.userid,
                taskId = id,
                isCompleted = newCompleted
            )
            if (newPoints != null) {
                val updatedUser = user.copy(points = newPoints)
                UserSession.update(updatedUser)
                sessionRepository.saveUser(updatedUser)
                pointsRepository.setPoints(updatedUser.userid, newPoints)
            } else {
                Log.w(logTag, "Toggle failed, reverting task $id")
                _tasks.update { current ->
                    current.map { task ->
                        if (task.id == id) task.copy(completed = taskBefore.completed) else task
                    }
                }
            }
            loadTasks(user.userid)
        }
    }

    fun removeTask(id: Int) {
        val user = UserSession.user.value ?: return
        viewModelScope.launch {
            val updated = taskRepository.deleteTask(userId = user.userid, taskId = id)
            _tasks.value = updated
        }
    }

    fun moveTask(id: Int, delta: Int) {
        _tasks.update { current ->
            val index = current.indexOfFirst { it.id == id }
            if (index == -1) return@update current
            val newIndex = (index + delta).coerceIn(0, current.lastIndex)
            if (index == newIndex) return@update current
            val mutable = current.toMutableList()
            val item = mutable.removeAt(index)
            mutable.add(newIndex, item)
            mutable.toList()
        }
    }
}

package com.example.lumaka.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumaka.data.repository.PointsRepository
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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    fun addTask(title: String, category: CategoryEnum) {
        val trimmed = title.trim()
        if (trimmed.isBlank()) return
        val nextId = (_tasks.value.maxOfOrNull { it.id } ?: 0) + 1
        _tasks.update { current -> current + Task(id = nextId, title = trimmed, category = category) }
    }

    fun toggleTask(id: Int) {
        val taskBefore = _tasks.value.firstOrNull { it.id == id } ?: return
        _tasks.update { current ->
            current.map { task ->
                if (task.id == id) task.copy(completed = !task.completed) else task
            }
        }
        val delta = if (taskBefore.completed) -5 else 5
        val currentUser = UserSession.user.value
        if (currentUser != null) {
            val newPoints = (currentUser.points + delta).coerceAtLeast(0)
            val updatedUser = currentUser.copy(points = newPoints)
            UserSession.update(updatedUser)
            viewModelScope.launch {
                sessionRepository.saveUser(updatedUser)
                pointsRepository.setPoints(currentUser.email, newPoints)
            }
        }
    }

    fun removeTask(id: Int) {
        _tasks.update { current -> current.filterNot { it.id == id } }
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

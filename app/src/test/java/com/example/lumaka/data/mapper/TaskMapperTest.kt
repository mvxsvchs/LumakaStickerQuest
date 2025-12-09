package com.example.lumaka.data.mapper

import com.example.lumaka.data.remote.dto.TaskResponse
import com.example.lumaka.domain.model.CategoryEnum
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskMapperTest {

    @Test
    fun toDomain_mapsUnknownCategoryToAllAndKeepsFields() {
        // Prüft, dass unbekannte Kategorien auf ALL fallen und alle Felder korrekt gemappt bleiben.
        val response = TaskResponse(
            taskId = 10,
            taskDescription = "Mystery task",
            categoryId = 999,
            isCompleted = true,
            position = 3,
            pointsReward = 50
        )

        val domain = response.toDomain()

        assertEquals(10, domain.id)
        assertEquals("Mystery task", domain.title)
        assertEquals(CategoryEnum.ALL, domain.category)
        assertEquals(true, domain.completed)
        assertEquals(3, domain.position)
        assertEquals(50, domain.pointsReward)
    }
}

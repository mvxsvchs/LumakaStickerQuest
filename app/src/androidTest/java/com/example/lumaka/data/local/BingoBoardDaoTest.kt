package com.example.lumaka.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BingoBoardDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: BingoBoardDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = database.bingoBoardDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertAndDeleteCells_respectsWeekBoundaries() = runBlocking {
        val email = "user@lumaka.app"

        dao.upsertBoard(BingoBoardEntity(email, "week-1", "sticker-a"))
        dao.upsertCells(
            listOf(
                BingoCellEntity(email, "week-1", 1, unlocked = true, stickerResId = 101),
                BingoCellEntity(email, "week-1", 2, unlocked = false, stickerResId = null)
            )
        )

        dao.upsertBoard(BingoBoardEntity(email, "week-2", "sticker-b"))
        dao.deleteCellsNotInWeek(email, "week-2")
        dao.upsertCells(listOf(BingoCellEntity(email, "week-2", 3, unlocked = true, stickerResId = 202)))

        val board = dao.getBoard(email)
        val week2Cells = dao.getCellsForWeek(email, "week-2")
        val week1Cells = dao.getCellsForWeek(email, "week-1")

        assertEquals("week-2", board?.weekKey)
        assertEquals("sticker-b", board?.lastSticker)
        assertEquals(listOf(3), week2Cells.map { it.cellId })
        assertEquals(emptyList<BingoCellEntity>(), week1Cells)
    }
}

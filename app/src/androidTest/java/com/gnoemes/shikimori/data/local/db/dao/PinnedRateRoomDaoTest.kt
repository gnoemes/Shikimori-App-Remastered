package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gnoemes.shikimori.data.local.db.AppDatabase
import com.gnoemes.shikimori.entity.rates.data.PinnedRateDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PinnedRateRoomDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: PinnedRateRoomDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.pinnedRateDao()
    }

    @Test
    fun insertAndGetPinnedRates() {
        val rate = PinnedRateDao(
            id = 1L,
            type = "anime",
            status = "watching",
            order = 0
        )
        dao.insert(rate).blockingAwait()

        val result = dao.getPinnedRates("anime", "watching").blockingGet()
        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("anime", result[0].type)
        assertEquals("watching", result[0].status)
        assertEquals(0, result[0].order)
    }

    @Test
    fun getPinnedRates_filteredByTypeAndStatus() {
        val rate1 = PinnedRateDao(id = 1L, type = "anime", status = "watching", order = 0)
        val rate2 = PinnedRateDao(id = 2L, type = "anime", status = "completed", order = 1)
        val rate3 = PinnedRateDao(id = 3L, type = "manga", status = "watching", order = 2)

        dao.insert(rate1).blockingAwait()
        dao.insert(rate2).blockingAwait()
        dao.insert(rate3).blockingAwait()

        val animeWatching = dao.getPinnedRates("anime", "watching").blockingGet()
        assertEquals(1, animeWatching.size)
        assertEquals(1L, animeWatching[0].id)

        val animeCompleted = dao.getPinnedRates("anime", "completed").blockingGet()
        assertEquals(1, animeCompleted.size)
        assertEquals(2L, animeCompleted[0].id)

        val mangaWatching = dao.getPinnedRates("manga", "watching").blockingGet()
        assertEquals(1, mangaWatching.size)
        assertEquals(3L, mangaWatching[0].id)
    }

    @Test
    fun deleteById() {
        val rate = PinnedRateDao(id = 1L, type = "anime", status = "watching", order = 0)
        dao.insert(rate).blockingAwait()

        dao.deleteById(1L).blockingAwait()

        val result = dao.getPinnedRates("anime", "watching").blockingGet()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getPinnedRates_returnsEmptyList_whenNoMatchingRates() {
        val result = dao.getPinnedRates("anime", "nonexistent").blockingGet()
        assertTrue(result.isEmpty())
    }

    @Test
    fun insertReplace_shouldUpdateExistingPinnedRate() {
        val rate1 = PinnedRateDao(id = 1L, type = "anime", status = "watching", order = 0)
        dao.insert(rate1).blockingAwait()

        val rate2 = PinnedRateDao(id = 1L, type = "anime", status = "watching", order = 5)
        dao.insert(rate2).blockingAwait()

        val result = dao.getPinnedRates("anime", "watching").blockingGet()
        assertEquals(1, result.size)
        assertEquals(5, result[0].order)
    }

    @Test
    fun deleteById_onlyRemovesSpecifiedRate() {
        val rate1 = PinnedRateDao(id = 1L, type = "anime", status = "watching", order = 0)
        val rate2 = PinnedRateDao(id = 2L, type = "anime", status = "completed", order = 1)
        dao.insert(rate1).blockingAwait()
        dao.insert(rate2).blockingAwait()

        dao.deleteById(1L).blockingAwait()

        val result = dao.getPinnedRates("anime", "completed").blockingGet()
        assertEquals(1, result.size)
        assertEquals(2L, result[0].id)
    }

    @After
    fun teardown() {
        database.close()
    }
}

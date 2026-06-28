package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gnoemes.shikimori.data.local.db.AppDatabase
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.NoSuchElementException

@RunWith(AndroidJUnit4::class)
class AnimeRateSyncRoomDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: AnimeRateSyncRoomDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.animeRateSyncDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetRate() {
        val entity = AnimeRateSyncDao(
            rateId = 1L,
            animeId = 100L,
            episodes = 24
        )
        dao.insert(entity).blockingAwait()

        val result = dao.getRate(1L).blockingGet()
        assertNotNull(result)
        assertEquals(1L, result.rateId)
        assertEquals(100L, result.animeId)
        assertEquals(24, result.episodes)
    }

    @Test
    fun insertAndGetEpisodeCount() {
        val entity = AnimeRateSyncDao(
            rateId = 1L,
            animeId = 100L,
            episodes = 24
        )
        dao.insert(entity).blockingAwait()

        val result = dao.getEpisodeCount(100L).blockingGet()
        assertEquals(24, result.toInt())
    }

    @Test
    fun insertReplace_shouldUpdateEpisodes() {
        val entity1 = AnimeRateSyncDao(
            rateId = 1L,
            animeId = 100L,
            episodes = 24
        )
        dao.insert(entity1).blockingAwait()

        val entity2 = AnimeRateSyncDao(
            rateId = 1L,
            animeId = 100L,
            episodes = 12
        )
        dao.insert(entity2).blockingAwait()

        val result = dao.getEpisodeCount(100L).blockingGet()
        assertEquals(12, result.toInt())
    }

    @Test
    fun deleteByAnimeId() {
        val entity = AnimeRateSyncDao(
            rateId = 1L,
            animeId = 100L,
            episodes = 24
        )
        dao.insert(entity).blockingAwait()

        dao.deleteByAnimeId(100L).blockingAwait()

        val testObserver = dao.getRate(1L).test()
        testObserver.assertComplete()
        testObserver.assertNoValues()
    }

    @Test
    fun getRate_notFound_returnsEmpty() {
        val testObserver = dao.getRate(999L).test()
        testObserver.assertComplete()
        testObserver.assertNoValues()
    }

    @Test
    fun getEpisodeCount_notFound_returnsEmpty() {
        val testObserver = dao.getEpisodeCount(999L).test()
        testObserver.assertComplete()
        testObserver.assertNoValues()
    }

    @Test
    fun insertMulipleRatesAndQueryEpisodeCount() {
        val entity1 = AnimeRateSyncDao(
            rateId = 1L,
            animeId = 100L,
            episodes = 24
        )
        val entity2 = AnimeRateSyncDao(
            rateId = 2L,
            animeId = 100L,
            episodes = 12
        )
        dao.insert(entity1).blockingAwait()
        dao.insert(entity2).blockingAwait()

        val result = dao.getEpisodeCount(100L).blockingGet()
        // Should return episodes from the second insert (replace) since same animeId
        assertEquals(12, result.toInt())
    }
}

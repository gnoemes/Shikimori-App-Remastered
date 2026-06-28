package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.EmptyResultSetException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gnoemes.shikimori.data.local.db.AppDatabase
import com.gnoemes.shikimori.entity.series.data.EpisodeDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EpisodeRoomDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: EpisodeRoomDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.episodeDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertSingleAndGetEpisode() {
        val episode = EpisodeDao(
            animeId = 1L,
            episodeId = 5,
            isWatched = 1
        )
        dao.insert(episode).blockingAwait()

        val result = dao.getEpisode(1L, 5).blockingGet()
        assertNotNull(result)
        assertEquals(1L, result.animeId)
        assertEquals(5, result.episodeId)
        assertEquals(1, result.isWatched)
    }

    @Test
    fun insertListAndGetAllEpisodes() {
        val episodes = listOf(
            EpisodeDao(animeId = 1L, episodeId = 1, isWatched = 1),
            EpisodeDao(animeId = 1L, episodeId = 2, isWatched = 0),
            EpisodeDao(animeId = 1L, episodeId = 3, isWatched = 1)
        )
        dao.insert(episodes).blockingAwait()

        val result = dao.getAllEpisodes().blockingGet()
        assertEquals(3, result.size)
    }

    @Test
    fun getWatchedEpisodesCount() {
        val episodes = listOf(
            EpisodeDao(animeId = 1L, episodeId = 1, isWatched = 1),
            EpisodeDao(animeId = 1L, episodeId = 2, isWatched = 1),
            EpisodeDao(animeId = 1L, episodeId = 3, isWatched = 0)
        )
        dao.insert(episodes).blockingAwait()

        val watchedCount = dao.getWatchedEpisodesCount(1L, 1).blockingGet()
        assertEquals(2, watchedCount.toInt())

        val unwatchedCount = dao.getWatchedEpisodesCount(1L, 0).blockingGet()
        assertEquals(1, unwatchedCount.toInt())
    }

    @Test
    fun deleteByAnimeId() {
        val episodes = listOf(
            EpisodeDao(animeId = 1L, episodeId = 1, isWatched = 1),
            EpisodeDao(animeId = 1L, episodeId = 2, isWatched = 0)
        )
        dao.insert(episodes).blockingAwait()

        dao.deleteByAnimeId(1L).blockingAwait()

        val result = dao.getAllEpisodes().blockingGet()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getEpisodesByWatchStatus() {
        val episodes = listOf(
            EpisodeDao(animeId = 1L, episodeId = 1, isWatched = 1),
            EpisodeDao(animeId = 1L, episodeId = 2, isWatched = 0),
            EpisodeDao(animeId = 1L, episodeId = 3, isWatched = 1)
        )
        dao.insert(episodes).blockingAwait()

        val watchedEpisodes = dao.getEpisodesByWatchStatus(1L, 1).blockingGet()
        assertEquals(2, watchedEpisodes.size)
        assertTrue(watchedEpisodes.all { it.isWatched == 1 })

        val unwatchedEpisodes = dao.getEpisodesByWatchStatus(1L, 0).blockingGet()
        assertEquals(1, unwatchedEpisodes.size)
        assertEquals(2, unwatchedEpisodes[0].episodeId)
    }

    @Test(expected = EmptyResultSetException::class)
    fun getEpisode_notFound_throws() {
        dao.getEpisode(999L, 999).blockingGet()
    }

    @Test
    fun deleteByAnimeId_keepsOtherAnimeEpisodes() {
        val episodesAnime1 = listOf(
            EpisodeDao(animeId = 1L, episodeId = 1, isWatched = 1)
        )
        val episodesAnime2 = listOf(
            EpisodeDao(animeId = 2L, episodeId = 1, isWatched = 0)
        )
        dao.insert(episodesAnime1).blockingAwait()
        dao.insert(episodesAnime2).blockingAwait()

        dao.deleteByAnimeId(1L).blockingAwait()

        val result = dao.getAllEpisodes().blockingGet()
        assertEquals(1, result.size)
        assertEquals(2L, result[0].animeId)
    }

    @Test
    fun getAllEpisodes_returnsEmptyList_whenNoEpisodes() {
        val result = dao.getAllEpisodes().blockingGet()
        assertTrue(result.isEmpty())
    }
}

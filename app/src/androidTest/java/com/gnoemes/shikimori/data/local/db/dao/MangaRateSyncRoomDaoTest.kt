package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gnoemes.shikimori.data.local.db.AppDatabase
import com.gnoemes.shikimori.entity.rates.data.MangaRateSyncDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MangaRateSyncRoomDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: MangaRateSyncRoomDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.mangaRateSyncDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetRate() {
        val entity = MangaRateSyncDao(
            rateId = 1L,
            mangaId = 100L,
            chapters = 50
        )
        dao.insert(entity).blockingAwait()

        val result = dao.getRate(1L).blockingGet()
        assertNotNull(result)
        assertEquals(1L, result.rateId)
        assertEquals(100L, result.mangaId)
        assertEquals(50, result.chapters)
    }

    @Test
    fun insertAndGetChaptersCount() {
        val entity = MangaRateSyncDao(
            rateId = 1L,
            mangaId = 100L,
            chapters = 50
        )
        dao.insert(entity).blockingAwait()

        val result = dao.getChaptersCount(100L).blockingGet()
        assertEquals(50, result.toInt())
    }

    @Test
    fun insertReplace_shouldUpdateChapters() {
        val entity1 = MangaRateSyncDao(
            rateId = 1L,
            mangaId = 100L,
            chapters = 50
        )
        dao.insert(entity1).blockingAwait()

        val entity2 = MangaRateSyncDao(
            rateId = 1L,
            mangaId = 100L,
            chapters = 25
        )
        dao.insert(entity2).blockingAwait()

        val result = dao.getChaptersCount(100L).blockingGet()
        assertEquals(25, result.toInt())
    }

    @Test
    fun deleteByMangaId() {
        val entity = MangaRateSyncDao(
            rateId = 1L,
            mangaId = 100L,
            chapters = 50
        )
        dao.insert(entity).blockingAwait()

        dao.deleteByMangaId(100L).blockingAwait()

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
    fun getChaptersCount_notFound_returnsEmpty() {
        val testObserver = dao.getChaptersCount(999L).test()
        testObserver.assertComplete()
        testObserver.assertNoValues()
    }

    @Test
    fun deleteByMangaId_keepsOtherMangaRates() {
        val entity1 = MangaRateSyncDao(
            rateId = 1L,
            mangaId = 100L,
            chapters = 50
        )
        val entity2 = MangaRateSyncDao(
            rateId = 2L,
            mangaId = 200L,
            chapters = 30
        )
        dao.insert(entity1).blockingAwait()
        dao.insert(entity2).blockingAwait()

        dao.deleteByMangaId(100L).blockingAwait()

        val result = dao.getRate(2L).blockingGet()
        assertNotNull(result)
        assertEquals(200L, result.mangaId)
        assertEquals(30, result.chapters)
    }
}

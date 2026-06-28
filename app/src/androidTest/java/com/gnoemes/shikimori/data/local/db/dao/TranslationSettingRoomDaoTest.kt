package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gnoemes.shikimori.data.local.db.AppDatabase
import com.gnoemes.shikimori.entity.series.data.TranslationSettingDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TranslationSettingRoomDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: TranslationSettingRoomDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.translationSettingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetSetting() {
        val setting = TranslationSettingDao(
            animeId = 1L,
            author = "Test Author",
            type = "subtitles"
        )
        dao.insert(setting).blockingAwait()

        val result = dao.getSetting(1L).blockingGet()
        assertNotNull(result)
        assertEquals(1L, result.animeId)
        assertEquals("Test Author", result.author)
        assertEquals("subtitles", result.type)
    }

    @Test
    fun insertReplace_shouldUpdate() {
        val setting1 = TranslationSettingDao(
            animeId = 1L,
            author = "Author 1",
            type = "subtitles"
        )
        dao.insert(setting1).blockingAwait()

        val setting2 = TranslationSettingDao(
            animeId = 1L,
            author = "Author 2",
            type = "dubbing"
        )
        dao.insert(setting2).blockingAwait()

        val result = dao.getSetting(1L).blockingGet()
        assertEquals("Author 2", result.author)
        assertEquals("dubbing", result.type)
    }

    @Test
    fun insertWithNullFields() {
        val setting = TranslationSettingDao(
            animeId = 1L,
            author = null,
            type = null
        )
        dao.insert(setting).blockingAwait()

        val result = dao.getSetting(1L).blockingGet()
        assertNotNull(result)
        assertEquals(1L, result.animeId)
        assertEquals(null, result.author)
        assertEquals(null, result.type)
    }

    @Test
    fun getSetting_notFound_returnsEmpty() {
        val testObserver = dao.getSetting(999L).test()
        testObserver.assertComplete()
        testObserver.assertNoValues()
    }

    @Test
    fun insertMulipleSettings_getByAnimeId() {
        val setting1 = TranslationSettingDao(
            animeId = 1L,
            author = "Author 1",
            type = "subtitles"
        )
        val setting2 = TranslationSettingDao(
            animeId = 2L,
            author = "Author 2",
            type = "dubbing"
        )
        dao.insert(setting1).blockingAwait()
        dao.insert(setting2).blockingAwait()

        val result1 = dao.getSetting(1L).blockingGet()
        assertEquals("Author 1", result1.author)

        val result2 = dao.getSetting(2L).blockingGet()
        assertEquals("Author 2", result2.author)
    }
}

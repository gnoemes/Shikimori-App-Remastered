package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.EmptyResultSetException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gnoemes.shikimori.data.local.db.AppDatabase
import com.gnoemes.shikimori.entity.chapters.ChapterDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChapterRoomDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: ChapterRoomDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.chapterDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetChapter() {
        val chapter = ChapterDao(
            mangaId = 1L,
            chapterId = 5,
            isReaded = 1
        )
        dao.insert(chapter).blockingAwait()

        val result = dao.getChapter(1L, 5L).blockingGet()
        assertNotNull(result)
        assertEquals(1L, result.mangaId)
        assertEquals(5, result.chapterId)
        assertEquals(1, result.isReaded)
    }

    @Test
    fun insertAndGetReadedChapterCount() {
        val chapter1 = ChapterDao(mangaId = 1L, chapterId = 1, isReaded = 1)
        val chapter2 = ChapterDao(mangaId = 1L, chapterId = 2, isReaded = 1)
        val chapter3 = ChapterDao(mangaId = 1L, chapterId = 3, isReaded = 0)

        dao.insert(chapter1).blockingAwait()
        dao.insert(chapter2).blockingAwait()
        dao.insert(chapter3).blockingAwait()

        val readedCount = dao.getReadedChapterCount(1L).blockingGet()
        assertEquals(2, readedCount.toInt())
    }

    @Test
    fun getAllChapters_returnsAllInserted() {
        val chapter1 = ChapterDao(mangaId = 1L, chapterId = 1, isReaded = 1)
        val chapter2 = ChapterDao(mangaId = 2L, chapterId = 2, isReaded = 0)

        dao.insert(chapter1).blockingAwait()
        dao.insert(chapter2).blockingAwait()

        val result = dao.getAllChapters().blockingGet()
        assertEquals(2, result.size)
        assertTrue(result.any { it.mangaId == 1L && it.chapterId == 1 })
        assertTrue(result.any { it.mangaId == 2L && it.chapterId == 2 })
    }

    @Test
    fun deleteByMangaId() {
        val chapter1 = ChapterDao(mangaId = 1L, chapterId = 1, isReaded = 1)
        val chapter2 = ChapterDao(mangaId = 1L, chapterId = 2, isReaded = 0)

        dao.insert(chapter1).blockingAwait()
        dao.insert(chapter2).blockingAwait()

        dao.deleteByMangaId(1L).blockingAwait()

        val result = dao.getAllChapters().blockingGet()
        assertTrue(result.isEmpty())
    }

    @Test(expected = EmptyResultSetException::class)
    fun getChapter_notFound_throws() {
        dao.getChapter(999L, 999L).blockingGet()
    }

    @Test
    fun deleteByMangaId_onlyRemovesSpecificManga() {
        val chapter1 = ChapterDao(mangaId = 1L, chapterId = 1, isReaded = 1)
        val chapter2 = ChapterDao(mangaId = 2L, chapterId = 1, isReaded = 0)

        dao.insert(chapter1).blockingAwait()
        dao.insert(chapter2).blockingAwait()

        dao.deleteByMangaId(1L).blockingAwait()

        val result = dao.getAllChapters().blockingGet()
        assertEquals(1, result.size)
        assertEquals(2L, result[0].mangaId)
    }

    @Test
    fun getReadedChapterCount_returnsZeroForNoReadedChapters() {
        val chapter = ChapterDao(mangaId = 1L, chapterId = 1, isReaded = 0)
        dao.insert(chapter).blockingAwait()

        val count = dao.getReadedChapterCount(1L).blockingGet()
        assertEquals(0, count.toInt())
    }
}

package com.gnoemes.shikimori.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gnoemes.shikimori.data.local.db.table.ChapterTable
import com.gnoemes.shikimori.entity.chapters.ChapterDao
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ChapterRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chapter: ChapterDao): Completable

    @Query("SELECT * FROM ${ChapterTable.TABLE} WHERE ${ChapterTable.COLUMN_MANGA_ID} = :mangaId AND ${ChapterTable.COLUMN_CHAPTER_ID} = :chapterId")
    fun getChapter(mangaId: Long, chapterId: Long): Single<ChapterDao>

    @Query("SELECT COUNT(*) FROM ${ChapterTable.TABLE} WHERE ${ChapterTable.COLUMN_MANGA_ID} = :mangaId")
    fun getReadedChapterCount(mangaId: Long): Single<Int>

    @Query("SELECT * FROM ${ChapterTable.TABLE}")
    fun getAllChapters(): Single<List<ChapterDao>>

    @Query("DELETE FROM ${ChapterTable.TABLE} WHERE ${ChapterTable.COLUMN_MANGA_ID} = :mangaId")
    fun deleteByMangaId(mangaId: Long): Completable
}

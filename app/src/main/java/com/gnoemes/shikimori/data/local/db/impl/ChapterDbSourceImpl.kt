package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.ChapterDbSource
import com.gnoemes.shikimori.data.local.db.dao.ChapterRoomDao
import com.gnoemes.shikimori.entity.chapters.ChapterDao
import com.gnoemes.shikimori.utils.toInt
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ChapterDbSourceImpl @Inject constructor(
        private val chapterDao: ChapterRoomDao
) : ChapterDbSource {

    override fun chapterReaded(mangaId: Long, chapterId: Int): Completable =
            chapterDao.insert(ChapterDao(mangaId, chapterId, true.toInt()))

    override fun isChapterReaded(mangaId: Long, chapterId: Long): Single<Boolean> =
            chapterDao.getChapter(mangaId, chapterId)
                    .map { it.isReaded == 1 }
                    .onErrorReturnItem(false)

    override fun getReadedChapterCount(mangaId: Long): Single<Int> =
            chapterDao.getReadedChapterCount(mangaId)
                    .onErrorReturnItem(0)

    override fun getReadedMangaIds(): Single<List<Long>> =
            chapterDao.getAllChapters()
                    .map { items -> items.reversed().map { it.mangaId }.distinct() }
                    .onErrorReturnItem(emptyList())


    override fun clearChapters(mangaId: Long): Completable =
            chapterDao.deleteByMangaId(mangaId)
}

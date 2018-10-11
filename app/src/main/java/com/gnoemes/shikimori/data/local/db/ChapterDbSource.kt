package com.gnoemes.shikimori.data.local.db

import io.reactivex.Completable
import io.reactivex.Single

interface ChapterDbSource {

//    fun saveChapters(chapters : List<Chapter>) : Completable

    fun chapterReaded(mangaId: Long, chapterId: Int): Completable

    fun isChapterReaded(mangaId: Long, chapterId: Long): Single<Boolean>

    fun getReadedChapterCount(mangaId: Long): Single<Int>

    fun getReadedMangaIds(): Single<List<Long>>

    fun clearChapters(mangaId: Long): Completable
}
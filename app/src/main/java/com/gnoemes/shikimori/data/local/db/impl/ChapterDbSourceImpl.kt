package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.ChapterDbSource
import com.gnoemes.shikimori.data.local.db.table.ChapterTable
import com.gnoemes.shikimori.entity.chapters.ChapterDao
import com.gnoemes.shikimori.utils.toBoolean
import com.gnoemes.shikimori.utils.toInt
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ChapterDbSourceImpl @Inject constructor(
        private val storIOSQLite: StorIOSQLite
) : ChapterDbSource {

    override fun chapterReaded(mangaId: Long, chapterId: Int): Completable =
            storIOSQLite
                    .put()
                    .`object`(ChapterDao(mangaId, chapterId, true.toInt()))
                    .prepare()
                    .asRxCompletable()

    override fun isChapterReaded(mangaId: Long, chapterId: Long): Single<Boolean> =
            storIOSQLite
                    .get()
                    .`object`(ChapterDao::class.java)
                    .withQuery(Query.builder()
                            .table(ChapterTable.TABLE)
                            .where("${ChapterTable.COLUMN_MANGA_ID} = ? AND ${ChapterTable.COLUMN_CHAPTER_ID} = ?")
                            .whereArgs(mangaId, chapterId)
                            .build())
                    .prepare()
                    .asRxSingle()
                    .map { it.get().isReaded!!.toBoolean()!! }
                    .onErrorReturnItem(false)

    override fun getReadedChapterCount(mangaId: Long): Single<Int> =
            storIOSQLite
                    .get()
                    .numberOfResults()
                    .withQuery(Query.builder()
                            .table(ChapterTable.TABLE)
                            .where("${ChapterTable.COLUMN_MANGA_ID} = ?")
                            .whereArgs(mangaId)
                            .build())
                    .prepare()
                    .asRxSingle()
                    .onErrorReturnItem(0)

    override fun getReadedMangaIds(): Single<List<Long>> =
            storIOSQLite
                    .get()
                    .listOfObjects(ChapterDao::class.java)
                    .withQuery(ChapterTable.ALL_QUERY)
                    .prepare()
                    .asRxSingle()
                    .map { items -> items.reversed().map { it.mangaId } }
                    .onErrorReturnItem(emptyList())


    override fun clearChapters(mangaId: Long): Completable =
            storIOSQLite
                    .delete()
                    .byQuery(DeleteQuery.builder()
                            .table(ChapterTable.TABLE)
                            .where("${ChapterTable.COLUMN_MANGA_ID} = ?")
                            .whereArgs(mangaId)
                            .build())
                    .prepare()
                    .asRxCompletable()
}
package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.MangaRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.table.MangaRateSyncTable
import com.gnoemes.shikimori.entity.rates.data.MangaRateSyncDao
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MangaRateSyncDbSourceImpl @Inject constructor(
        private val storIOSQLite: StorIOSQLite
) : MangaRateSyncDbSource {

    override fun getRate(rateId: Long): Single<UserRate> =
            storIOSQLite
                    .get()
                    .`object`(MangaRateSyncDao::class.java)
                    .withQuery(Query.builder()
                            .table(MangaRateSyncTable.TABLE)
                            .where("${MangaRateSyncTable.COLUMN_RATE_ID} = ?")
                            .whereArgs(rateId)
                            .build())
                    .prepare()
                    .asRxSingle()
                    .map { it.get() }
                    .map { UserRate(it.rateId, targetId = it.mangaId, chapters = it.chapters) }

    override fun saveRate(userRate: UserRate): Completable =
            storIOSQLite
                    .put()
                    .`object`(MangaRateSyncDao(userRate.id!!, userRate.targetId!!, userRate.chapters!!))
                    .prepare()
                    .asRxCompletable()

    override fun getChaptersCount(mangaId: Long): Single<Int> =
            storIOSQLite
                    .get()
                    .`object`(MangaRateSyncDao::class.java)
                    .withQuery(Query.builder()
                            .table(MangaRateSyncTable.TABLE)
                            .where("${MangaRateSyncTable.COLUMN_MANGA_ID} = ?")
                            .whereArgs(mangaId)
                            .build())
                    .prepare()
                    .asRxSingle()
                    .map {
                        when (it.isPresent) {
                            true -> it.get().chapters
                            else -> 0
                        }
                    }

    override fun clearRate(mangaId: Long): Completable =
            storIOSQLite
                    .delete()
                    .byQuery(DeleteQuery.builder()
                            .table(MangaRateSyncTable.TABLE)
                            .where("${MangaRateSyncTable.COLUMN_MANGA_ID} = ?")
                            .whereArgs(mangaId)
                            .build())
                    .prepare()
                    .asRxCompletable()
}
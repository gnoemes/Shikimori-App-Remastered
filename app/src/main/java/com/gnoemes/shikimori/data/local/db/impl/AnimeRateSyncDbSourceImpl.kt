package com.gnoemes.shikimori.data.local.db.impl

import android.util.Log
import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.table.AnimeRateSyncTable
import com.gnoemes.shikimori.entity.rates.data.AnimeRateSyncDao
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AnimeRateSyncDbSourceImpl @Inject constructor(
        private val storIOSQLite: StorIOSQLite
) : AnimeRateSyncDbSource {

    override fun saveRate(userRate: UserRate): Completable =
            Completable.fromAction {
                val result = storIOSQLite
                        .put()
                        .`object`(AnimeRateSyncDao(userRate.id!!, userRate.targetId!!, userRate.episodes!!))
                        .prepare()
                        .executeAsBlocking()
                Log.i("DEVE", "${result.wasInserted()}  ${result.numberOfRowsUpdated()?.toString()}")
            }
                    .onErrorResumeNext { it.printStackTrace();Completable.complete() }


    override fun getEpisodeCount(animeId: Long): Single<Int> =
            storIOSQLite
                    .get()
                    .`object`(AnimeRateSyncDao::class.java)
                    .withQuery(Query.builder()
                            .table(AnimeRateSyncTable.TABLE)
                            .where("${AnimeRateSyncTable.COLUMN_ANIME_ID} = ?")
                            .whereArgs(animeId)
                            .build())
                    .prepare()
                    .asRxSingle()
                    .map {
                        when (it.isPresent) {
                            true -> it.get().episodes
                            else -> 0
                        }
                    }

    override fun clearRate(animeId: Long): Completable =
            storIOSQLite
                    .delete()
                    .byQuery(DeleteQuery.builder()
                            .table(AnimeRateSyncTable.TABLE)
                            .where("${AnimeRateSyncTable.COLUMN_ANIME_ID} = ?")
                            .whereArgs(animeId)
                            .build())
                    .prepare()
                    .asRxCompletable()
}
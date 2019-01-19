package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.local.db.table.EpisodeTable
import com.gnoemes.shikimori.entity.series.data.EpisodeDao
import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.utils.toBoolean
import com.gnoemes.shikimori.utils.toInt
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class EpisodeDbSourceImpl @Inject constructor(
        private val storIOSQLite: StorIOSQLite
) : EpisodeDbSource {

    override fun saveEpisodes(episodes: List<Episode>): Completable {
        val items = episodes.map { EpisodeDao(it.animeId, it.id, it.isWatched.toInt()) }

        return Completable.fromAction {
            val result = storIOSQLite
                    .put()
                    .objects(items)
                    .prepare()
                    .executeAsBlocking()
        }
    }

    override fun episodeWatched(animeId: Long, episodeId: Int): Completable =
            storIOSQLite
                    .put()
                    .`object`(EpisodeDao(animeId, episodeId, true.toInt()))
                    .prepare()
                    .asRxCompletable()

    override fun episodeUnWatched(animeId: Long, episodeId: Int): Completable =
            storIOSQLite
                    .put()
                    .`object`(EpisodeDao(animeId, episodeId, false.toInt()))
                    .prepare()
                    .asRxCompletable()

    override fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean> =
            storIOSQLite
                    .get()
                    .`object`(EpisodeDao::class.java)
                    .withQuery(Query.builder()
                            .table(EpisodeTable.TABLE)
                            .where("${EpisodeTable.COLUMN_ANIME_ID} = ? AND ${EpisodeTable.COLUMN_EPISODE_ID} = ?")
                            .whereArgs(animeId, episodeId)
                            .build())
                    .prepare()
                    .asRxSingle()
                    .map { it.get().isWatched?.toBoolean()!! }
                    .onErrorReturnItem(false)


    override fun getWatchedEpisodesCount(animeId: Long): Single<Int> =
            storIOSQLite
                    .get()
                    .numberOfResults()
                    .withQuery(Query.builder()
                            .table(EpisodeTable.TABLE)
                            .where("${EpisodeTable.COLUMN_ANIME_ID} = ? AND ${EpisodeTable.COLUMN_IS_WATCHED} = ?")
                            .whereArgs(animeId, true.toInt())
                            .build())
                    .prepare()
                    .asRxSingle()
                    .onErrorReturnItem(0)

    override fun getWatchedAnimeIds(): Single<List<Long>> =
            storIOSQLite
                    .get()
                    .listOfObjects(EpisodeDao::class.java)
                    .withQuery(EpisodeTable.ALL_QUERY)
                    .prepare()
                    .asRxSingle()
                    .map { items -> items.reversed().map { it.animeId } }
                    .onErrorReturnItem(emptyList())


    override fun clearEpisodes(animeId: Long): Completable =
            storIOSQLite
                    .delete()
                    .byQuery(DeleteQuery.builder()
                            .table(EpisodeTable.TABLE)
                            .where("${EpisodeTable.COLUMN_ANIME_ID} = ?")
                            .whereArgs(animeId)
                            .build())
                    .prepare()
                    .asRxCompletable()
}
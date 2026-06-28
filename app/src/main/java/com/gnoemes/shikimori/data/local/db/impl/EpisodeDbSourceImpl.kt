package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.local.db.dao.EpisodeRoomDao
import com.gnoemes.shikimori.entity.series.data.EpisodeDao
import com.gnoemes.shikimori.entity.series.domain.Episode
import com.gnoemes.shikimori.utils.toInt
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class EpisodeDbSourceImpl @Inject constructor(
        private val episodeDao: EpisodeRoomDao
) : EpisodeDbSource {

    override fun saveEpisodes(episodes: List<Episode>): Completable {
        val items = episodes.map { EpisodeDao(it.animeId, it.index, it.isWatched.toInt()) }
        return episodeDao.insert(items)
    }

    override fun episodeWatched(animeId: Long, episodeId: Int): Completable =
            episodeDao.insert(EpisodeDao(animeId, episodeId, true.toInt()))

    override fun episodeUnWatched(animeId: Long, episodeId: Int): Completable =
            episodeDao.insert(EpisodeDao(animeId, episodeId, false.toInt()))

    override fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean> =
            episodeDao.getEpisode(animeId, episodeId)
                    .map { it.isWatched == 1 }
                    .onErrorReturnItem(false)


    override fun getWatchedEpisodesCount(animeId: Long): Single<Int> =
            episodeDao.getWatchedEpisodesCount(animeId, true.toInt())
                    .onErrorReturnItem(0)

    override fun getWatchedAnimeIds(): Single<List<Long>> =
            episodeDao.getAllEpisodes()
                    .map { items -> items.reversed().map { it.animeId }.distinct() }
                    .onErrorReturnItem(emptyList())


    override fun clearEpisodes(animeId: Long): Completable =
            episodeDao.deleteByAnimeId(animeId)

    override fun getFirstNotWatchedEpisodeIndex(animeId: Long): Single<Int> =
            episodeDao.getEpisodesByWatchStatus(animeId, false.toInt())
                    .map { it.firstOrNull()?.episodeId ?: 1 }
}

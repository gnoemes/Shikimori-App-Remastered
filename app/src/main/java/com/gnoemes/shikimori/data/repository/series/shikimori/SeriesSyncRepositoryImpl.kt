package com.gnoemes.shikimori.data.repository.series.shikimori

import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SeriesSyncRepositoryImpl @Inject constructor(
        private val episodeSource: EpisodeDbSource
) : SeriesSyncRepository {

    override fun setEpisodeStatus(animeId: Long, episodeId: Int, isWatched: Boolean): Completable =
            if (isWatched) episodeSource.episodeWatched(animeId, episodeId)
            else episodeSource.episodeUnWatched(animeId, episodeId)

    override fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean> = episodeSource.isEpisodeWatched(animeId, episodeId)

    override fun getWatchedEpisodesCount(animeId: Long): Single<Int> = episodeSource.getWatchedEpisodesCount(animeId)
}
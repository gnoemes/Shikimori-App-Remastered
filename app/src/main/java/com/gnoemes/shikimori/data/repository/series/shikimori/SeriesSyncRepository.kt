package com.gnoemes.shikimori.data.repository.series.shikimori

import io.reactivex.Completable
import io.reactivex.Single

interface SeriesSyncRepository {
    fun getWatchedEpisodesCount(animeId: Long): Single<Int>

    fun setEpisodeStatus(animeId: Long, episodeId: Int, isWatched: Boolean): Completable

    fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean>
}
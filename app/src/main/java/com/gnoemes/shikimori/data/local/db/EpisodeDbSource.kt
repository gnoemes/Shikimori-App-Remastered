package com.gnoemes.shikimori.data.local.db

import com.gnoemes.shikimori.entity.series.domain.Episode
import io.reactivex.Completable
import io.reactivex.Single

interface EpisodeDbSource {

    fun saveEpisodes(episodes: List<Episode>): Completable

    fun episodeWatched(animeId: Long, episodeId: Int): Completable

    fun episodeUnWatched(animeId: Long, episodeId: Int): Completable

    fun isEpisodeWatched(animeId: Long, episodeId: Int): Single<Boolean>

    fun getWatchedEpisodesCount(animeId: Long): Single<Int>

    fun getWatchedAnimeIds(): Single<List<Long>>

    fun clearEpisodes(animeId: Long): Completable
}
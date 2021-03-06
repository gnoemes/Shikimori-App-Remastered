package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import io.reactivex.Single

interface AnimeSource {

    fun getEpisodes(id: Long): Single<List<EpisodeResponse>>

    fun getEpisodesAlternative(id: Long): Single<List<EpisodeResponse>>

    fun getTranslations(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>>

    fun getTranslationsAlternative(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>>

    fun getVideo(animeId: Long, episodeId: Int, videoId: String, language: String, type: String, author: String, hosting: String): Single<VideoResponse>

    fun getVideoAlternative(translationId: Long): Single<VideoResponse>
}
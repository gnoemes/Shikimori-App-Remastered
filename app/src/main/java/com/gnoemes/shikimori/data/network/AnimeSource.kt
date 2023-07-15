package com.gnoemes.shikimori.data.network

import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Single

interface AnimeSource {

    fun getEpisodes(id: Long, name : String): Single<List<EpisodeResponse>>

    fun getEpisodesAlternative(id: Long, name : String): Single<List<EpisodeResponse>>

    fun getEpisodesShikicinema(id: Long): Single<List<EpisodeResponse>>

    fun getTranslations(animeId: Long, name: String, episodeId: Long, type: TranslationType): Single<List<TranslationResponse>>

    fun getTranslationsAlternative(animeId: Long, name: String, episodeId: Long, type: TranslationType): Single<List<TranslationResponse>>

    fun getTranslationsShikicinema(animeId: Long, episode: Long, type: TranslationType, loadLength: Boolean): Single<List<TranslationResponse>>

    fun getVideo(animeId: Long, episodeId: Int, videoId: String, language: String, type: TranslationType, author: String, hosting: String, url : String?): Single<VideoResponse>

    fun getVideoAlternative(translationId: Long, animeId: Long, episodeIndex : Long, token : String?): Single<VideoResponse>
}
package com.gnoemes.shikimori.data.network.impl

import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import io.reactivex.Single
import javax.inject.Inject

class CloudAnimeSourceImpl @Inject constructor(private val api: VideoApi) : AnimeSource {

    override fun getEpisodes(id: Long): Single<List<EpisodeResponse>> = api.getEpisodes(id)

    override fun getEpisodesAlternative(id: Long): Single<List<EpisodeResponse>> = api.getEpisodesAlternative(id)

    override fun getTranslations(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>> = api.getTranslations(animeId, episodeId, type)

    override fun getTranslationsAlternative(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>> = api.getTranslationsAlternative(animeId, episodeId, type)

    override fun getVideo(animeId: Long, episodeId: Int, videoId: String, language: String, type: String, author: String, hosting: String): Single<VideoResponse> = api.getVideo(animeId, episodeId, videoId, language, type, author, hosting)

    override fun getVideoAlternative(translationId: Long, animeId: Long, episodeIndex : Long): Single<VideoResponse> = api.getVideoAlternative(translationId)
            .map { it.copy(animeId = animeId, episodeId = episodeIndex) }
}
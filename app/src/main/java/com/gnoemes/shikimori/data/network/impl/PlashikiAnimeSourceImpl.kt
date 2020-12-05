package com.gnoemes.shikimori.data.network.impl

import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.PlashikiVideoApi
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import io.reactivex.Single
import javax.inject.Inject

class PlashikiAnimeSourceImpl @Inject constructor(
        private val api: VideoApi,
        private val plashikiApi: PlashikiVideoApi
) : AnimeSource {

    override fun getEpisodes(id: Long): Single<List<EpisodeResponse>> {
        return plashikiApi.getTranslations(id)
                .map { it.result ?: emptyList() }
                .map { list -> list.map { EpisodeResponse(it) } }
                .map { it.distinctBy { it.index } }
    }

    override fun getTranslations(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>> {
        return plashikiApi.getTranslations(animeId)
                .map { it.result ?: emptyList() }
                .map { list ->
                    val translations = mutableListOf<TranslationResponse>()
                    val episodesSize = list.distinctBy { it.episode }.size

                    list
                            .filter { it.type.type == type }
                            .forEach { response ->
                                val id =
                                        if (response.hosting is VideoHosting.SMOTRET_ANIME) {
                                            response.url?.split("/")?.lastOrNull()?.toLong() ?: response.id
                                        } else response.id
                                translations.add(TranslationResponse(id, response, episodesSize))
                            }

                    translations
                }
    }

    override fun getVideo(animeId: Long, episodeId: Int, videoId: String, language: String, type: String, author: String, hosting: String): Single<VideoResponse> = api.getVideo(animeId, episodeId, videoId, language, type, author, hosting)

    override fun getEpisodesAlternative(id: Long): Single<List<EpisodeResponse>> = api.getEpisodesAlternative(id)

    override fun getTranslationsAlternative(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>> = api.getTranslationsAlternative(animeId, episodeId, type)

    override fun getVideoAlternative(translationId: Long, animeId: Long, episodeIndex: Long, token: String?): Single<VideoResponse> = api.getVideoAlternative(translationId, token)
            .map { it.copy(animeId = animeId, episodeId = episodeIndex) }
}
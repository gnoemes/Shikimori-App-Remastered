package com.gnoemes.shikimori.data.network.impl

import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.DocumentVideoApi
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.data.repository.series.shikimori.DynamicAgentRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.converter.DocumentParsingConverter
import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import io.reactivex.Single
import javax.inject.Inject

class LocalAnimeSourceImpl @Inject constructor(
        private val api: VideoApi,
        private val parseApi : DocumentVideoApi,
        private val converter: DocumentParsingConverter,
        private val agentRepository: DynamicAgentRepository
) : AnimeSource {

    override fun getEpisodes(id: Long): Single<List<EpisodeResponse>> =
            parseApi.getEpisodes(id, agentRepository.getAgent())
                    .map { converter.convertEpisodes(it, id) }

    override fun getTranslations(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>> =
            api.getTranslations(animeId, episodeId, agentRepository.getAgent())

    override fun getVideo(animeId: Long, episodeId: Int, videoId: String, language: String, type: String, author: String, hosting: String): Single<VideoResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEpisodesAlternative(id: Long): Single<List<EpisodeResponse>> = api.getEpisodesAlternative(id)

    override fun getTranslationsAlternative(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>> = api.getTranslationsAlternative(animeId, episodeId, type)

    override fun getVideoAlternative(translationId: Long): Single<VideoResponse> = api.getVideoAlternative(translationId)
}
package com.gnoemes.shikimori.data.network.impl

import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.ShimoriVideoApi
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Single
import javax.inject.Inject
import kotlin.random.Random

class ShimoriAnimeSourceImpl @Inject constructor(
        private val api: VideoApi,
        private val shimoriApi: ShimoriVideoApi
) : AnimeSource {

    override fun getEpisodes(id: Long, name: String): Single<List<EpisodeResponse>> {
        return shimoriApi.getEpisodes(id, name)
                .map { episodes ->
                    (0..episodes).map {
                        EpisodeResponse(
                                it.toLong(),
                                it,
                                id,
                                emptyList(),
                                "",
                                emptyList()
                        )
                    }
                }
    }

    override fun getTranslations(animeId: Long, name: String, episodeId: Long, type: TranslationType): Single<List<TranslationResponse>> {
        val shimoriType = getShimoriType(type)

        return shimoriApi.getTranslations(animeId, name, episodeId.toInt(), shimoriType)
                .map { list ->
                    list.map { response ->
                        val id = Random.nextLong()
                        TranslationResponse(id, response)
                    }
                }
    }

    override fun getVideo(animeId: Long, episodeId: Int, videoId: String, language: String, type: TranslationType, author: String, hosting: String, url: String?): Single<VideoResponse> {
        val shimoriType = getShimoriType(type)

        return shimoriApi.getVideo(animeId, episodeId, shimoriType, author, hosting, videoId.toLongOrNull(), url)
    }

    override fun getEpisodesAlternative(id: Long, name: String): Single<List<EpisodeResponse>> =
            shimoriApi.getSeries(id, name)
                    .map { response ->
                        response.map {
                            EpisodeResponse(
                                    it.id,
                                    it.index.toInt(),
                                    it.animeId,
                                    emptyList(),
                                    "",
                                    emptyList()
                            )
                        }
                    }

    override fun getTranslationsAlternative(animeId: Long, episodeId: Long, type: String): Single<List<TranslationResponse>> = api.getTranslationsAlternative(animeId, episodeId, type)

    override fun getVideoAlternative(translationId: Long, animeId: Long, episodeIndex: Long, token: String?): Single<VideoResponse> = api.getVideoAlternative(translationId, token)
            .map { it.copy(animeId = animeId, episodeId = episodeIndex) }


    private fun getShimoriType(type: TranslationType) = when (type) {
        TranslationType.VOICE_RU -> "dub"
        TranslationType.SUB_RU -> "subs"
        else -> "raw"
    }
}
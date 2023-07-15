package com.gnoemes.shikimori.data.network.impl

import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.ShikicinemaVideoApi
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
        private val shimoriApi: ShimoriVideoApi,
        private val shikicinemaVideoApi: ShikicinemaVideoApi
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

        return shimoriApi.getTranslations(animeId, name, episodeId.toInt(), 1, shimoriType)
                .map { list ->
                    list.map { response ->
                        val id = Random.nextLong()
                        TranslationResponse(id, response)
                    }
                }
    }

    override fun getVideo(animeId: Long, episodeId: Int, videoId: String, language: String, type: TranslationType, author: String, hosting: String, url: String?): Single<VideoResponse> {
        val shimoriType = getShimoriType(type)

        return shimoriApi.getVideo(animeId, episodeId, shimoriType, author, hosting, 1, videoId.toLongOrNull(), url, null)
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

    override fun getTranslationsAlternative(animeId: Long, name: String, episodeId: Long, type: TranslationType): Single<List<TranslationResponse>> {
        val shimoriType = getShimoriType(type)

        return shimoriApi.getTranslations(animeId, name, episodeId.toInt(), 2, shimoriType)
                .map { list ->
                    list.map { response ->
                        TranslationResponse(response.id, response)
                    }
                }
    }


    override fun getVideoAlternative(translationId: Long, animeId: Long, episodeIndex: Long, token: String?): Single<VideoResponse> {
        return shimoriApi.getVideo(
                animeId,
                episodeIndex.toInt(),
                "dub",
                "none",
                "none",
                2,
                translationId,
                null,
                accessToken = token
        )
    }

    override fun getEpisodesShikicinema(id: Long): Single<List<EpisodeResponse>> {
        return shikicinemaVideoApi.getEpisodes(id)
                .map { response ->
                    (0..response.length).map {
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

    override fun getTranslationsShikicinema(animeId: Long, episodeId: Long, type: TranslationType, loadLength: Boolean): Single<List<TranslationResponse>> {
        val shikicinemaType = getShikicinemaType(type)

        return if (loadLength) shikicinemaVideoApi.getEpisodes(animeId).flatMap { lengthResponse ->
            shikicinemaVideoApi.getTranslations(animeId, "all", episodeId, shikicinemaType)
                    .map { list ->
                        list.map { response ->
                            TranslationResponse(response, lengthResponse.length)
                        }
                    }
        }
        else shikicinemaVideoApi.getTranslations(animeId, "all", episodeId, shikicinemaType)
                .map { list ->
                    list.map { response ->
                        TranslationResponse(response, 0)
                    }
                }
    }


    private fun getShimoriType(type: TranslationType) = when (type) {
        TranslationType.VOICE_RU -> "dub"
        TranslationType.SUB_RU -> "subs"
        else -> "raw"
    }

    private fun getShikicinemaType(type: TranslationType) = when (type) {
        TranslationType.VOICE_RU -> "озвучка"
        TranslationType.SUB_RU -> "субтитры"
        else -> "оригинал"
    }
}


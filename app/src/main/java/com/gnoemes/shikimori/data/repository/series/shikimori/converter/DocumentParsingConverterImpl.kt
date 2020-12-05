package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.app.domain.HttpStatusCode
import com.gnoemes.shikimori.entity.app.domain.exceptions.ServiceCodeException
import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.data.VideoRequest
import com.gnoemes.shikimori.entity.series.domain.TranslationQuality
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.gnoemes.shikimori.utils.Utils
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class DocumentParsingConverterImpl @Inject constructor(
        private val gson: Gson
) : DocumentParsingConverter {

    private companion object {
        const val INFO_OBJECT_QUERY = "gon.watch_online="
        const val EPISODES_QUERY = "div.c-anime_video_episodes>[data-episode],div.b-show_more-more>[data-episode]"
        const val EPISODE_ID_QUERY = "data-episode"
        const val EPISODE_TRANSLATIONS_QUERY = "episode-kinds"
        const val EPISODE_HOSTINGS_QUERY = "episode-hostings"

        const val TRANSLATIONS_QUERY = "div.b-video_variant[data-video_id]"
        const val TRANSLATIONS_ALL_QUERY = "div.video-variant-group[data-kind=%s]"
        const val VIDEO_ID_QUERY = "data-video_id"
        const val VIDEO_QUALITY_QUERY = "video-quality"
        const val VIDEO_TYPE_QUERY = "video-kind"
        const val VIDEO_HOSTING_QUERY = "video-hosting"
        const val VIDEO_AUTHOR_QUERY = "video-author"

        const val VIDEO_URL_QUERY = "div.video-link a"
    }

    data class InfoObject(
            @SerializedName("is_licensed") val isLicensed: Boolean,
            @SerializedName("is_censored") val isCensored: Boolean
    )

    override fun convertEpisodes(it: Document, animeId: Long): List<EpisodeResponse> {

        val infoJson = it.select("script").last().html().let {
            it.substring(
                    it.indexOf(INFO_OBJECT_QUERY) + INFO_OBJECT_QUERY.length,
                    it.lastIndexOf("};") + 1)
        }
        var errorItem: InfoObject? = null

        try {
            errorItem = gson.fromJson(infoJson, InfoObject::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return when {
            errorItem != null && errorItem.isLicensed -> throw ServiceCodeException(HttpStatusCode.FORBIDDED)
            errorItem != null && errorItem.isCensored -> throw ServiceCodeException(HttpStatusCode.NOT_FOUND)
            else -> it.select(EPISODES_QUERY).map { convertEpisode(animeId, it) }
        }
    }

    override fun convertTranslations(it: Document, animeId: Long, episodeId: Long, type: String): List<TranslationResponse> {
        val episodesSize = convertEpisodes(it, animeId).size
        return it.select(String.format(TRANSLATIONS_ALL_QUERY, type))
                .first()
                .select(TRANSLATIONS_QUERY)
                .map { convertTranslation(it, animeId, episodeId, episodesSize) }
    }

    override fun convertVideoRequest(it: Document, animeId: Long, episodeId: Int): VideoRequest {
        val playerUrl = it.select(VIDEO_URL_QUERY).first().attr("href").let { if (it.contains("http")) it else "http:$it" }
        return VideoRequest(animeId, episodeId.toLong(), playerUrl)
    }

    override fun convertCookie(language: String, type: String, author: String, hosting: String): String =
            "anime_video_language=$language; anime_video_kind=$type; anime_video_author=$author; anime_video_hosting=$hosting; path=/; domain=.play.shikimori.org; Expires=Tue, 19 Jan 2038 03:14:07 GMT;"

    private fun convertTranslation(e: Element, animeId: Long, episodeId: Long, episodesSize: Int): TranslationResponse {
        val videoId = e.attr(VIDEO_ID_QUERY).toLong()
        val rawQuality = e.getElementsByClass(VIDEO_QUALITY_QUERY).attr("class").split(" ").getOrNull(1)
        val quality = TranslationQuality.values().find { it.equalQuality(rawQuality) }
                ?: TranslationQuality.TV
        val type = e.getElementsByClass(VIDEO_TYPE_QUERY).text().trim().toLowerCase().let { strType -> TranslationType.values().find { it.isEqualType(strType) } }
                ?: TranslationType.VOICE_RU
        val hosting = e.getElementsByClass(VIDEO_HOSTING_QUERY).text().trim().let { rawHosting -> Utils.hostingFromString(rawHosting).synonymType }
        val author = e.getElementsByClass(VIDEO_AUTHOR_QUERY).text().trim()

        return TranslationResponse(
                videoId,
                animeId,
                episodeId.toInt(),
                type,
                quality,
                hosting,
                author,
                episodesSize
        )
    }

    private fun convertEpisode(animeId: Long, e: Element): EpisodeResponse {
        val id = e.attr(EPISODE_ID_QUERY).toLong()
        val translations = e.getElementsByClass(EPISODE_TRANSLATIONS_QUERY)
                .text()
                .replace(" ", "")
                .split(",")
                .map { it.trim() }
                .map { convertTranslationType(it) }

        val rawHostings = e.getElementsByClass(EPISODE_HOSTINGS_QUERY).text()

        val hostings = rawHostings.replace(" ", "")
                .split(",")
                .map { it.trim() }
                .map { convertVideoHosting(it) }
                .map { it.synonymType }

        return EpisodeResponse(
                id,
                id.toInt(),
                animeId,
                translations,
                rawHostings,
                hostings
        )
    }

    private fun convertVideoHosting(hosting: String): VideoHosting = Utils.hostingFromString(hosting)

    private fun convertTranslationType(type: String): TranslationType = TranslationType.values().find { it.isEqualType(type) }
            ?: TranslationType.VOICE_RU

}
package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.app.domain.HttpStatusCode
import com.gnoemes.shikimori.entity.app.domain.exceptions.ServiceCodeException
import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
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

    override fun convertTranslations(it: Document): List<TranslationResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

        return EpisodeResponse(
                id,
                id.toInt(),
                animeId,
                translations,
                rawHostings,
                hostings
        )
    }

    private fun convertVideoHosting(hosting: String): VideoHosting = VideoHosting.values().find { it.isEqualType(hosting) }
            ?: VideoHosting.UNKNOWN

    private fun convertTranslationType(type: String): TranslationType = TranslationType.values().find { it.isEqualType(type) }
            ?: TranslationType.VOICE_RU

}
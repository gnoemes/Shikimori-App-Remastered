package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.OkVideosResponse
import com.gnoemes.shikimori.entity.series.data.ok.OkPlayerDataJsonModel
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class OkVideoConverterImpl @Inject constructor() : OkVideoConverter {

    override fun convertTracks(it: TranslationVideo, playerData: OkPlayerDataJsonModel): Video {
        val tracks = playerData.flashvars.metadata.videos.map {
            Track(getResolution(it.name)!!, it.url)
        }
        return Video(it.animeId, it.episodeIndex.toLong(), it.webPlayerUrl!!, it.videoHosting, tracks, null, null)
    }

    override fun parsePlaylists(html: String): OkPlayerDataJsonModel {
        val doc = Jsoup.parse(html)
        val playerDataJson = doc.select("div[data-module=\"OKVideo\"]").first().attr("data-options")
        return Json.decodeFromString(playerDataJson)
    }

    private fun getResolution(okQuality: String): String? {
        return when (okQuality) {
            "mobile" -> "144"
            "lowest" -> "240"
            "low" -> "360"
            "sd" -> "480"
            "hd" -> "720"
            "full" -> "1080"
            else -> null
        }
    }
}
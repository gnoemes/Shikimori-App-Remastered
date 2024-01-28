package com.gnoemes.shikimori.data.repository.series.shikimori.parser

import com.gnoemes.shikimori.entity.series.data.OkPlayerData
import com.gnoemes.shikimori.entity.series.data.OkPlayerFlashvarsMetadata
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.google.gson.Gson
import org.jsoup.Jsoup
import javax.inject.Inject

class OkParserImpl @Inject constructor() : OkParser {

    override fun video(video: TranslationVideo, tracks: List<Track>): Video =
            Video(video.animeId, video.episodeIndex.toLong(), video.webPlayerUrl!!, video.videoHosting, tracks, null, null)

    override fun tracks(html: String?): List<Track> {
        if (html.isNullOrEmpty()) return emptyList()

        val doc = Jsoup.parse(html)
        val playerDataJson = doc.select("div[data-module=\"OKVideo\"]").first().attr("data-options")

        val gson = Gson()
        val playerData = gson.fromJson<OkPlayerData>(playerDataJson, OkPlayerData::class.java)
        val metadata = gson.fromJson<OkPlayerFlashvarsMetadata>(playerData.flashvars.metadata, OkPlayerFlashvarsMetadata::class.java)

        return metadata.videos
                .mapNotNull {
                    val quality = getResolution(it.name)
                    if (quality != null) Track(quality, it.url) else null
                }
                .sortedByDescending { it.quality.toInt() }
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
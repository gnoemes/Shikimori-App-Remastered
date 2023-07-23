package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.ok.OkPlayerDataJsonModel
import com.gnoemes.shikimori.entity.series.data.ok.OkPlayerDataMetadata
import com.gnoemes.shikimori.entity.series.data.ok.OkPlayerDataVideo
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.google.gson.Gson
import org.jsoup.Jsoup
import javax.inject.Inject

class OkVideoConverterImpl @Inject constructor() : OkVideoConverter {

    override fun convertTracks(it: TranslationVideo, videos: List<OkPlayerDataVideo>): Video {
        val tracks = videos.map { Track(getResolution(it.name)!!, it.url) }
        return Video(it.animeId, it.episodeIndex.toLong(), it.webPlayerUrl!!, it.videoHosting, tracks.reversed(), null, null)
    }

    override fun parsePlaylists(html: String?): List<OkPlayerDataVideo> {
        if (html.isNullOrEmpty()) return listOf();
        val doc = Jsoup.parse(html)
        val playerDataJson = doc.select("div[data-module=\"OKVideo\"]").first().attr("data-options")

        val gson = Gson()
        val playerData = gson.fromJson<OkPlayerDataJsonModel>(playerDataJson, OkPlayerDataJsonModel::class.java)
        val metadata = gson.fromJson<OkPlayerDataMetadata>(playerData.flashvars.metadata, OkPlayerDataMetadata::class.java)

        return metadata.videos
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
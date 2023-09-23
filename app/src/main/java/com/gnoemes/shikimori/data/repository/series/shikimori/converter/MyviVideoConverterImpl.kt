package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import org.jsoup.Jsoup
import javax.inject.Inject

class MyviVideoConverterImpl @Inject constructor() : MyviVideoConverter {

    override fun convertTracks(it: TranslationVideo, video : String?): Video {
        val tracks = mutableListOf<Track>()

        if (video !== null) {
            tracks.add(Track("unknown", video))
        }

        return Video(it.animeId, it.episodeIndex.toLong(), it.webPlayerUrl!!, it.videoHosting, tracks, null, null)
    }

    override fun parsePlaylist(html: String?): String? {
        if (html.isNullOrEmpty()) return null

        val doc = Jsoup.parse(html)
        val script = doc.select("script").find {
            it.data().contains("CreatePlayer(\"v")
        } ?: return null

        val scriptString = script.data().toString()
        var playlistUrl = scriptString
            .substringAfter("\"v=")
            .substringBefore("\\u0026tp=video")
            .replace("%26", "&")
            .replace("%3a", ":")
            .replace("%2f", "/")
            .replace("%3f", "?")
            .replace("%3d", "=")

        if (!playlistUrl.startsWith("https:")) {
            playlistUrl = "https:$playlistUrl"
        }

        return playlistUrl
    }
}
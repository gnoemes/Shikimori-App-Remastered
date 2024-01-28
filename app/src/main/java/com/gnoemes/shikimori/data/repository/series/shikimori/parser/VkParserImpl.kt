package com.gnoemes.shikimori.data.repository.series.shikimori.parser

import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import javax.inject.Inject

class VkParserImpl @Inject constructor() : VkParser {

    override fun video(video: TranslationVideo, tracks: List<Track>): Video =
            Video(video.animeId, video.episodeIndex.toLong(), video.webPlayerUrl!!, video.videoHosting, tracks, null, null)

    override fun tracks(html: String?): List<Track> {
        if (html == null) return emptyList()

        val regex = Regex("\"(url240|url360|url480|url720|url1080)\":\\s?\"(.*?)\"")
        val matches = regex.findAll(html)

        return matches
                .map { it.destructured.toList() }
                .map {
                    val (key, value) = it
                    Track(key.replace("url", ""), value)
                }
                .toList()
                .sortedByDescending { it.quality.toInt() }
    }
}
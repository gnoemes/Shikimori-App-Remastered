package com.gnoemes.shikimori.data.repository.series.shikimori.parser

import android.os.Build
import androidx.annotation.RequiresApi
import com.gnoemes.shikimori.entity.series.data.NuumStreamsMetadataResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import javax.inject.Inject
import io.lindstrom.m3u8.parser.MasterPlaylistParser

class NuumParserImpl @Inject constructor() : NuumParser {

    override fun video(video: TranslationVideo, tracks: List<Track>): Video =
            Video(video.animeId, video.episodeIndex.toLong(), video.webPlayerUrl!!, video.videoHosting, tracks, null, null)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun tracks(m3uContent: String?): List<Track> {
        if (m3uContent.isNullOrEmpty()) return emptyList()

        val parser = MasterPlaylistParser()
        val playlist = parser.readPlaylist(m3uContent.replace("\r", ""))

        return playlist.variants()
                .map {
                    val quality = it.resolution().get().height().toString()
                    Track(quality, it.uri())
                }
                .sortedByDescending { it.quality.toInt() }
    }

    override fun getMasterPlaylistUrl(metadata: NuumStreamsMetadataResponse?): String? {
        return metadata
                ?.result
                ?.mediaContainerStreams
                ?.flatMap { it.streamMedia }
                ?.mapNotNull { it.mediaMeta.mediaArchiveUrl.takeIf { url -> url.contains("master.m3u8") } }
                ?.firstOrNull()
    }

    override fun getMetadataUrl(playerUrl: String): String {
        val videoId = playerUrl
                .replace("\\/+$".toRegex(), "")
                .split("/")
                .last()
        return "https://nuum.ru/api/v2/media-containers/$videoId"
    }
}
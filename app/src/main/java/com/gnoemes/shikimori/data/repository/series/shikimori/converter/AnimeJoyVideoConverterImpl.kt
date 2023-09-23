package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.AnimeJoyFileResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.utils.toUri
import javax.inject.Inject

class AnimeJoyVideoConverterImpl @Inject constructor() : AnimeJoyVideoConverter {

    override fun convertTracks(it: TranslationVideo, videos: AnimeJoyFileResponse): Video {
        val tracks = mutableListOf<Track>()

        if (videos.src1080 != null) tracks.add(Track("1080", videos.src1080!!))
        if (videos.src720 != null) tracks.add(Track("720", videos.src720!!))
        if (videos.src480 != null) tracks.add(Track("480", videos.src480!!))
        if (videos.src360 != null) tracks.add(Track("360", videos.src360!!))
        if (videos.src240 != null) tracks.add(Track("240", videos.src240!!))

        return Video(it.animeId, it.episodeIndex.toLong(), it.webPlayerUrl!!, it.videoHosting, tracks, null, null)
    }

    override fun parsePlaylists(embedUrl: String?): AnimeJoyFileResponse {
        val fileResponse = AnimeJoyFileResponse(null, null, null, null, null)
        if (embedUrl.isNullOrEmpty()) return fileResponse

        embedUrl.toUri().getQueryParameter("file")?.split(",")?.forEach {
            val quality = it.substringAfter("[").substringBefore("]")
            val videoUrl = it.substringAfter("]")

            when (quality) {
                "240p"-> fileResponse.src240 = videoUrl
                "360p"-> fileResponse.src360 = videoUrl
                "480p"-> fileResponse.src480 = videoUrl
                "720p"-> fileResponse.src720 = videoUrl
                "1080p"-> fileResponse.src1080 = videoUrl
            }
        }

        return fileResponse
    }
}
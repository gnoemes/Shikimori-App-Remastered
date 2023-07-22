package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.OkVideosResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import javax.inject.Inject

class OkVideoConverterImpl @Inject constructor() : OkVideoConverter {

    override fun convertTracks(it: TranslationVideo, response: OkVideosResponse): Video {
        val tracks = response.tracks.map { Track(it.quality, it.url) }
        return Video(it.animeId, it.episodeIndex.toLong(), it.webPlayerUrl!!, it.videoHosting, tracks, null, null)
    }
}
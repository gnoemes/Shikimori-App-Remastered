package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.TrackResponse
import com.gnoemes.shikimori.entity.series.data.VideoResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import javax.inject.Inject

class VideoResponseConverterImpl @Inject constructor(
): VideoResponseConverter {

    override fun apply(t: VideoResponse): Video =
            convertVideo(t)

    private fun convertVideo(t: VideoResponse): Video {
        val tracks = t.tracks.map { convertTrack(it) }
        return Video(
                t.animeId,
                t.episodeId,
                t.player,
                t.hosting,
                tracks,
                t.subAss,
                t.subVtt
        )
    }

    private fun convertTrack(it: TrackResponse): Track =
            Track(it.quality, it.url)

}
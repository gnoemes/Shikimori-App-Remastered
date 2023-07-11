package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.SovetRomanticaVideosResponse
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import javax.inject.Inject

class SovetRomanticaVideoConverterImpl @Inject constructor() : SovetRomanticaVideoConverter {

    override fun convertTracks(it: Video, response: SovetRomanticaVideosResponse): Video {
        val tracks = mutableListOf<Track>()

        if (response.src1080 != null) tracks.add(Track("1080", response.src1080))
        if (response.src720 != null) tracks.add(Track("720", response.src720))
        if (response.src480 != null) tracks.add(Track("480", response.src480))

        return Video(it.animeId, it.episodeId, it.player, it.hosting, tracks, null, null)
    }
}
package com.gnoemes.shikimori.data.repository.series.shikimori.parser

import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.shimori.dto.nuum.NuumStreamsMetadataResponse

interface NuumParser {

    fun video(video: TranslationVideo, tracks: List<Track>): Video

    fun tracks(m3uContent: String?): List<Track>

    fun getMasterPlaylistUrl(metadata: NuumStreamsMetadataResponse?): String?

    fun getMetadataUrl(playerUrl: String): String
}
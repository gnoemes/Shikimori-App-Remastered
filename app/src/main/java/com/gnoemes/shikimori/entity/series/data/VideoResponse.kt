package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.google.gson.annotations.SerializedName

data class VideoResponse(
        @field:SerializedName("animeId") val animeId: Long,
        @field:SerializedName("episodeId") val episodeId: Long,
        @field:SerializedName("player") val player : String,
        @field:SerializedName("hosting") val hosting: VideoHosting,
        @field:SerializedName("tracks") val tracks: List<TrackResponse>
)
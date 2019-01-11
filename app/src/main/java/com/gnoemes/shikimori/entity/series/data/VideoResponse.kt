package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class VideoResponse(
        @field:SerializedName("animeId") val animeId: Long,
        @field:SerializedName("episodeId") val episodeId: Long,
        @field:SerializedName("tracks") val tracks: List<TrackResponse>
)
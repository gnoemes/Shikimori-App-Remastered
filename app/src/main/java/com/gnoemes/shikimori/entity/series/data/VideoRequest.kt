package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class VideoRequest(
        @SerializedName("animeId") val animeId: Long,
        @SerializedName("episodeId") val episodeId: Long,
        @SerializedName("playerUrl") val playerUrl: String
)
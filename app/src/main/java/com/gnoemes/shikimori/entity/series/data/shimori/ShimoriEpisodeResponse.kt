package com.gnoemes.shikimori.entity.series.data.shimori

import com.google.gson.annotations.SerializedName

data class ShimoriEpisodeResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("index") val index: Long,
        @SerializedName("animeId") val animeId: Long
)
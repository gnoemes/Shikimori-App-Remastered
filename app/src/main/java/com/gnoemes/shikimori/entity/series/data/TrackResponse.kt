package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class TrackResponse(
        @field:SerializedName("quality") val quality : String,
        @field:SerializedName("url") val url : String
)
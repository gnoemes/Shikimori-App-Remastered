package com.gnoemes.shikimori.entity.series.data.ok

import com.google.gson.annotations.SerializedName

data class OkPlayerDataVideo(
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String
)

package com.gnoemes.shikimori.entity.series.data.ok

import com.google.gson.annotations.SerializedName

data class OkPlayerDataMetadata(
        @SerializedName("videos") val videos: List<OkPlayerDataVideo>
)

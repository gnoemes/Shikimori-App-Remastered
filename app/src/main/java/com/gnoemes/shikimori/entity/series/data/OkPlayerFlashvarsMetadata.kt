package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class OkPlayerFlashvarsMetadata(
        @SerializedName("videos") val videos: List<Video>
) {
    data class Video(
            @SerializedName("name") val name: String,
            @SerializedName("url") val url: String
    )
}

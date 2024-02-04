package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class MailRuVideosResponse (
        @SerializedName("videos") val videos : List<Video>
) {
    data class Video (
            @SerializedName("url") val url : String,
            @SerializedName("key") val key : String
    )
}
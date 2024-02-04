package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class MailRuPlayerData(
        @SerializedName("video") val video: Video
) {
    data class Video(
            @SerializedName("metadataUrl") val metadataUrl: String
    )
}

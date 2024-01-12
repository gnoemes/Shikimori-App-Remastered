package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class MailRuVideoResponse (
        @SerializedName("url") val url : String,
        @SerializedName("key") val key : String
)
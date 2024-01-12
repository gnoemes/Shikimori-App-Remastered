package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class MailRuVideosResponse (
        @SerializedName("videos") val videos : List<MailRuVideoResponse>
)
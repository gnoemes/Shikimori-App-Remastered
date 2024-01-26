package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class MailRuPlayerDataJsonModel(
        @SerializedName("video") val video: MailRuPlayerDataVideo
)

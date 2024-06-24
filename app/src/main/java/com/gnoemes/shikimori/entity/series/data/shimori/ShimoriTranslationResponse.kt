package com.gnoemes.shikimori.entity.series.data.shimori

import com.gnoemes.shikimori.entity.series.domain.TranslationQuality
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.google.gson.annotations.SerializedName

data class ShimoriTranslationResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("kind") private val _kind: String?,
        @SerializedName("targetId") val targetId: Long,
        @SerializedName("episode") val episode: Int,
        @SerializedName("url") val url: String,
        @SerializedName("hosting") val hosting: String,
        @SerializedName("language") val language: String?,
        @SerializedName("author") val author: String?,
        @SerializedName("quality") private val _quality: String?,
        @SerializedName("episodesTotal") val episodesTotal: Int?,
        @SerializedName("adLink") val adLink : String?
) {
    val quality: TranslationQuality
        get() = when (_quality) {
            "bd" -> TranslationQuality.BD
            else -> TranslationQuality.TV
        }

    val kind: TranslationType
        get() = when (_kind) {
            "subs" -> TranslationType.SUB_RU
            "dub" -> TranslationType.VOICE_RU
            else -> TranslationType.RAW
        }

}

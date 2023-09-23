package com.gnoemes.shikimori.entity.series.data.shikicinema

import com.gnoemes.shikimori.entity.series.domain.TranslationQuality
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.google.gson.annotations.SerializedName
import java.net.URL

data class ShikicinemaTranslationResponse (
        @SerializedName("id") val id: Long,
        @SerializedName("url") val url: String,
        @SerializedName("anime_id") val anime_id: Long,
        @SerializedName("anime_english") private val anime_english: String,
        @SerializedName("anime_russian") private val anime_russian: String,
        @SerializedName("episode") val episode: Int,
        @SerializedName("kind") private val _kind: String,
        @SerializedName("language") private val language: String,
        @SerializedName("quality") val _quality: String?,
        @SerializedName("author") val author: String?,
        @SerializedName("watches_count") val watches_count: Long?,
        @SerializedName("uploader") val uploader: String
) {
    val quality: TranslationQuality
        get() = when (_quality?.toLowerCase()) {
            "bd" -> TranslationQuality.BD
            "dvd" -> TranslationQuality.DVD
            else -> TranslationQuality.TV
        }

    val kind: TranslationType
        get() = when (_kind) {
            "субтитры" -> TranslationType.SUB_RU
            "озвучка" -> TranslationType.VOICE_RU
            else -> TranslationType.RAW
        }

    val hosting: String
        get() = URL(url).host
}

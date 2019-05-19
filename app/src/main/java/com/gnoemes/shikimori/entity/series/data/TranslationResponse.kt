package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.entity.series.domain.TranslationQuality
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.google.gson.annotations.SerializedName

data class TranslationResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("animeId") val animeId: Long,
        @field:SerializedName("episodeId") val episodeId: Int,
        @field:SerializedName("type") private val _type: TranslationType?,
        @field:SerializedName("quality") val quality: TranslationQuality,
        @field:SerializedName("hosting") val _hosting: VideoHosting?,
        @field:SerializedName("author") val author: String,
        @field:SerializedName("episodesSize") val episodesSize: Int
) {
    val hosting: VideoHosting
        get() = _hosting ?: VideoHosting.UNKNOWN

    val type: TranslationType
        get() = _type ?: TranslationType.VOICE_RU
}
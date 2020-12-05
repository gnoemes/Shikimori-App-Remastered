package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.entity.series.data.plashiki.PlashikiTranslationResponse
import com.gnoemes.shikimori.entity.series.domain.TranslationQuality
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.gnoemes.shikimori.utils.Utils
import com.google.gson.annotations.SerializedName

data class TranslationResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("animeId") val animeId: Long,
        @field:SerializedName("episodeId") val episodeId: Int,
        @field:SerializedName("type") private val _type: TranslationType?,
        @field:SerializedName("quality") val quality: TranslationQuality,
        @field:SerializedName("hosting") val _hosting: String?,
        @field:SerializedName("author") val author: String,
        @field:SerializedName("episodesSize") val episodesSize: Int
) {

    constructor(id: Long, response: PlashikiTranslationResponse, episodesSize: Int) : this(
            id,
            response.animeId,
            response.episode,
            response.type,
            response.quality,
            response.rawHosting,
            response.author ?: "",
            episodesSize
    )

    val hosting: VideoHosting
        get() = Utils.hostingFromString(_hosting)

    val type: TranslationType
        get() = _type ?: TranslationType.VOICE_RU
}
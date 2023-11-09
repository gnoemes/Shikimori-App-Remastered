package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.entity.series.data.shimori.ShimoriTranslationResponse
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
        @field:SerializedName("episodesSize") val episodesSize: Int,
        val webPlayerUrl: String? = null
) {

    constructor(id: Long, response: ShimoriTranslationResponse) : this(
            id,
            response.targetId,
            response.episode,
            response.kind,
            response.quality,
            response.hosting,
            response.author ?: "",
            response.episodesTotal ?: 0,
            response.url
    )

    val hosting: VideoHosting
        get() = Utils.hostingFromString(_hosting)

    val type: TranslationType
        get() = _type ?: TranslationType.VOICE_RU
}
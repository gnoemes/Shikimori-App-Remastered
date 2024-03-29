package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.gnoemes.shikimori.utils.Utils
import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("index") val index: Int,
        @field:SerializedName("animeId") val animeId: Long,
        @field:SerializedName("translations") val translations: List<TranslationType>,
        @field:SerializedName("rawHostings") val rawHosting: String,
        @field:SerializedName("videoHostings") private val _hostings: List<String>
) {
    val hostings: List<VideoHosting>
        get() = _hostings.map { Utils.hostingFromString(it) }

}
package com.gnoemes.shikimori.entity.series.data.plashiki

import com.gnoemes.shikimori.entity.series.domain.TranslationQuality
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.gnoemes.shikimori.utils.Utils
import com.google.gson.annotations.SerializedName
import java.net.URL

data class PlashikiTranslationResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("kind") private val kind: TranslationType?,
        @SerializedName("lang") val lang: String,
        @SerializedName("author") val author: String?,
        @SerializedName("url") val url: String?,
        @SerializedName("anime_id") val animeId: Long,
        @SerializedName("episode") val episode: Int,
        @SerializedName("quality") private val _quality: TranslationQuality?
) {

    val hosting : VideoHosting
        get() = URL(url).host.let { Utils.hostingFromString(it) }

    val rawHosting : String?
        get() = URL(url).host

    val type: TranslationType
        get() = kind ?: TranslationType.VOICE_RU

    val quality : TranslationQuality
        get() = _quality ?: TranslationQuality.TV

}
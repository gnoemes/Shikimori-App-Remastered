package com.gnoemes.shikimori.entity.anime.data

import com.gnoemes.shikimori.entity.anime.domain.AnimeVideoType
import com.google.gson.annotations.SerializedName

class AnimeVideoResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String?,
        @field:SerializedName("url") val url: String,
        @field:SerializedName("image_url") val imageUrl: String?,
        @field:SerializedName("kind") private val _type: AnimeVideoType?,
        @field:SerializedName("hosting") val hosting: String?
) {
    val type: AnimeVideoType
        get() = _type ?: AnimeVideoType.OTHER
}
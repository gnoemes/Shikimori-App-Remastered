package com.gnoemes.shikimori.entity.roles.data

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.google.gson.annotations.SerializedName

data class CharacterDetailsResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("image") val image: ImageResponse,
        @field:SerializedName("url") val url: String,
        @field:SerializedName("altname") val nameAlt: String?,
        @field:SerializedName("japanese") val nameJp: String?,
        @field:SerializedName("description") val description: String?,
        @field:SerializedName("description_source") val descriptionSource: String?,
        @field:SerializedName("seyu") val seyu: List<PersonResponse>?,
        @field:SerializedName("animes") val animes: List<AnimeResponse>,
        @field:SerializedName("mangas") val mangas: List<MangaResponse>
)
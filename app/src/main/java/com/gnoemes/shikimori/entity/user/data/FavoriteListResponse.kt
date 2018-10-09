package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class FavoriteListResponse(
        @field:SerializedName("animes") val animes: List<FavoriteResponse>,
        @field:SerializedName("mangas") val mangas: List<FavoriteResponse>,
        @field:SerializedName("characters") val characters: List<FavoriteResponse>,
        @field:SerializedName("people") val people: List<FavoriteResponse>,
        @field:SerializedName("mangakas") val mangakas: List<FavoriteResponse>,
        @field:SerializedName("seyu") val seyu: List<FavoriteResponse>,
        @field:SerializedName("producers") val producers: List<FavoriteResponse>
)
package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(
        @field:SerializedName("id") val id : Long,
        @field:SerializedName("name") val name : String,
        @field:SerializedName("russian") val nameRu : String?,
        @field:SerializedName("image") val image : String,
        @field:SerializedName("url") val url : String
)
package com.gnoemes.shikimori.entity.common.data

import com.google.gson.annotations.SerializedName

data class GenreResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("kind") val type: String?
)
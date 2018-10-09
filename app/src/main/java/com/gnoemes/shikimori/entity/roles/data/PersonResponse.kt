package com.gnoemes.shikimori.entity.roles.data

import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.google.gson.annotations.SerializedName

data class PersonResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("image") val image: ImageResponse,
        @field:SerializedName("url") val url: String
)
package com.gnoemes.shikimori.entity.common.data

import com.google.gson.annotations.SerializedName

data class LinkResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("kind") val name: String?,
        @field:SerializedName("url") val url: String
)
package com.gnoemes.shikimori.entity.forum.data

import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.google.gson.annotations.SerializedName

data class ForumResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name : String,
        @field:SerializedName("permalink") val type : ForumType,
        @field:SerializedName("url") val url : String
)
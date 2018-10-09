package com.gnoemes.shikimori.entity.user.data

import com.gnoemes.shikimori.entity.common.domain.Type
import com.google.gson.annotations.SerializedName

data class StatusResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("size") val size: Int,
        @field:SerializedName("type") val type: Type
)
package com.gnoemes.shikimori.entity.club

import com.gnoemes.shikimori.entity.common.data.ImageResponse
import com.google.gson.annotations.SerializedName

data class ClubResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("logo") val image: ImageResponse,
        @field:SerializedName("is_censored") val isCensored: Boolean,
        @field:SerializedName("join_policy") val policyJoin: ClubPolicy?,
        @field:SerializedName("comment_policy") val policyComment: ClubPolicy?
)
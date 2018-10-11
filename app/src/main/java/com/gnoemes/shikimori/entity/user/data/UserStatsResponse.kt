package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class UserStatsResponse(
        @field:SerializedName("full_statuses") val status : FullStatusResponse,
        @field:SerializedName("has_anime?") val hasAnime: Boolean,
        @field:SerializedName("has_manga?") val hasManga: Boolean
)
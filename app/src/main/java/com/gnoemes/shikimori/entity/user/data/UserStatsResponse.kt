package com.gnoemes.shikimori.entity.user.data

import com.google.gson.annotations.SerializedName

data class UserStatsResponse(
        @field:SerializedName("full_statuses") val status : FullStatusResponse,
        @field:SerializedName("scores") val scores: StatResponse,
        @field:SerializedName("types") val types: StatResponse,
        @field:SerializedName("ratings") val ratings: StatResponse,
        @field:SerializedName("has_anime?") val hasAnime: Boolean,
        @field:SerializedName("has_manga?") val hasManga: Boolean
)
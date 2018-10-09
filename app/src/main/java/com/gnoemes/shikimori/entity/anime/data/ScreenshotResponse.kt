package com.gnoemes.shikimori.entity.anime.data

import com.google.gson.annotations.SerializedName

data class ScreenshotResponse(
        @field:SerializedName("original") val original: String?,
        @field:SerializedName("preview") val preview: String?
)
package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class VkFileResponse(
        @SerializedName("mp4_240") val src240 : String?,
        @SerializedName("mp4_360") val src360 : String?,
        @SerializedName("mp4_480") val src480 : String?,
        @SerializedName("mp4_720") val src720 : String?,
        @SerializedName("mp4_1080") val src1080 : String?
)
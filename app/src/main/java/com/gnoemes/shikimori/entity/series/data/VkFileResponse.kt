package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class VkFileResponse(
        @SerializedName("mp4_240") var src240 : String?,
        @SerializedName("mp4_360") var src360 : String?,
        @SerializedName("mp4_480") var src480 : String?,
        @SerializedName("mp4_720") var src720 : String?,
        @SerializedName("mp4_1080") var src1080 : String?
)
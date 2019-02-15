package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class VkVideoResponse(
        @SerializedName("files") val file : VkFileResponse?
)
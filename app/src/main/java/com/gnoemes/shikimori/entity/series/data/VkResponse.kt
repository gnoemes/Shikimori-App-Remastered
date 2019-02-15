package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class VkResponse(
        @SerializedName("response") val response: VkVideoItemsResponse
)
package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class VkVideoItemsResponse(
        @SerializedName("items") val items : List<VkVideoResponse>
)
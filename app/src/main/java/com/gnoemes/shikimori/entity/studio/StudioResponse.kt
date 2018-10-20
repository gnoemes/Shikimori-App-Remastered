package com.gnoemes.shikimori.entity.studio

import com.google.gson.annotations.SerializedName

data class StudioResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("filtered_name") val nameFiltered: String,
        @field:SerializedName("real") val isReal: Boolean,
        @field:SerializedName("image") val imageUrl: String?
)
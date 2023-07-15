package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class SovetRomanticaVideosResponse (
        @SerializedName("src_720") val src720 : String?,
        @SerializedName("src_480") val src480 : String?,
        @SerializedName("src_1080") val src1080 : String?
)
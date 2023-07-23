package com.gnoemes.shikimori.entity.series.data.ok

import com.google.gson.annotations.SerializedName

data class OkPlayerDataJsonModel(
        @SerializedName("flashvars") val flashvars: OkPlayerDataFlashvars
)

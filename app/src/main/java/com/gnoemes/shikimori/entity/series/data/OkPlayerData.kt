package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class OkPlayerData(
        @SerializedName("flashvars") val flashvars: Flashvars
) {
    data class Flashvars(
            @SerializedName("metadata") val metadata: String
    )
}

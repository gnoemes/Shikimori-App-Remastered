package com.gnoemes.shikimori.entity.series.data

import com.google.gson.annotations.SerializedName

data class OkVideosResponse (
        @field:SerializedName("tracks") val tracks: List<TrackResponse>
)
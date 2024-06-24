package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import com.gnoemes.shikimori.utils.Utils
import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @field:SerializedName("animeId") val animeId: Long,
    @field:SerializedName("episodeId") val episodeId: Long,
    @field:SerializedName("player") val player: String,
    @field:SerializedName("hosting") private val _hosting: String?,
    @field:SerializedName("tracks") val tracks: List<TrackResponse>,
    @SerializedName("adLink") val adLink: String?,
    @SerializedName("subtitlesUrl") val subAss: String?,
    @SerializedName("subtitlesVttUrl") val subVtt: String?
) {
    val hosting: VideoHosting
        get() = Utils.hostingFromString(_hosting)
}
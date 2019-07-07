package com.gnoemes.shikimori.entity.series.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
        val animeId: Long,
        val episodeId: Long,
        val player : String,
        val hosting: VideoHosting,
        val tracks: List<Track>
) : Parcelable
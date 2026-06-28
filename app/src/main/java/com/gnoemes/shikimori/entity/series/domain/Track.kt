package com.gnoemes.shikimori.entity.series.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
        val quality : String,
        val url : String
) : Parcelable
package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.series.domain.Video
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeriesDownloadItem(
        val hosting: String,
        val quality: String?,
        val url: String,
        val video : Video
) : Parcelable
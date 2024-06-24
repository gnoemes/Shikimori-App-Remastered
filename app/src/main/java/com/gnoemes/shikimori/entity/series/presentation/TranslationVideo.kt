package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.gnoemes.shikimori.entity.series.domain.VideoHosting
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TranslationVideo(
        val videoId: Long,
        val animeId: Long,
        val episodeIndex: Int,
        val language: String,
        val author: String,
        val authorSimple: String,
        val type: TranslationType,
        val videoHosting: VideoHosting,
        val webPlayerUrl: String?,
        val adLink : String?
) : Parcelable
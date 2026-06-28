package com.gnoemes.shikimori.entity.anime.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenshotsNavigationData(
        val selected: Int,
        val items: List<Screenshot>
) : Parcelable
package com.gnoemes.shikimori.entity.anime.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Screenshot(
        val original: String?,
        val preview: String?
) : Parcelable
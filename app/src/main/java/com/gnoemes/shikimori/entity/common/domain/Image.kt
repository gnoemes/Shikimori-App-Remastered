package com.gnoemes.shikimori.entity.common.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
        val original: String?,
        val preview: String?,
        val x96: String?,
        val x48: String?
) : Parcelable
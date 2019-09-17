package com.gnoemes.shikimori.entity.common.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommonNavigationData(
        val id : Long,
        val type : Type
) : Parcelable
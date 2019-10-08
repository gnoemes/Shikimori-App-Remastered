package com.gnoemes.shikimori.entity.user.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserStatisticItem(
        val category : String,
        val count : Int,
        val progress : Float
) : Parcelable
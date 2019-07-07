package com.gnoemes.shikimori.entity.user.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserHistoryNavigationData(
        val id : Long,
        val name : String?
) : Parcelable
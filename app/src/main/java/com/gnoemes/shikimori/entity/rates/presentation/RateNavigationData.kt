package com.gnoemes.shikimori.entity.rates.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RateNavigationData(
        val userId : Long,
        val type : Type,
        val status : RateStatus
) : Parcelable
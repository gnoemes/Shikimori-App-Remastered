package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Image
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseSeriesNavigationData(
        val animeId : Long,
        val image : Image,
        val name : String
) : Parcelable
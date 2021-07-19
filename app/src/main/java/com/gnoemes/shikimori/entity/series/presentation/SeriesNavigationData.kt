package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Image
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeriesNavigationData(
        val animeId: Long,
        val image: Image,
        val name: String,
        val nameEng : String,
        val rateId: Long?,
        val episodesAired : Int,
        val episode : Int?
) : Parcelable
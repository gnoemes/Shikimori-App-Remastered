package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class EpisodesNavigationData(
        val animeId: Long,
        val name : String,
        val currentEpisode: Int,
        val rateId: Long?,
        val isAlternative: Boolean
) : Parcelable
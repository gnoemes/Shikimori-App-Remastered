package com.gnoemes.shikimori.entity.series.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmbeddedPlayerNavigationData(
        val animeName: String,
        val rateId : Long?,
        val episodesSize : Int,
        val payload: TranslationVideo,
        val nameEng: String,
        val isAlternative: Boolean
) : Parcelable
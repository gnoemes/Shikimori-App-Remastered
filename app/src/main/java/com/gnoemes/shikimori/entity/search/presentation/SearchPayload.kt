package com.gnoemes.shikimori.entity.search.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Genre
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchPayload(
        val genre: Genre? = null,
        val studioId: Long?= null
) : Parcelable
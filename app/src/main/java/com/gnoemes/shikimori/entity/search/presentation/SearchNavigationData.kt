package com.gnoemes.shikimori.entity.search.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.Type
import kotlinx.android.parcel.Parcelize

@Parcelize
class SearchNavigationData(
        val genre: Genre,
        val type: Type
) : Parcelable
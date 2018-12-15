package com.gnoemes.shikimori.entity.search.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterCategory(
        val category : String,
        val filters : MutableList<FilterItem>
) : Parcelable
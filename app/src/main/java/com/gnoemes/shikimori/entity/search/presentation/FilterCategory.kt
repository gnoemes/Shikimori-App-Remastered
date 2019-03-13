package com.gnoemes.shikimori.entity.search.presentation

import android.os.Parcelable
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.search.domain.FilterType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterCategory(
        val filterType: FilterType,
        val categoryLocalized : String,
        val filters : MutableList<FilterItem>
) : Parcelable
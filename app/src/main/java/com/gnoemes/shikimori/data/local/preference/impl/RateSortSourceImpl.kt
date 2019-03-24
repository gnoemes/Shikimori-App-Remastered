package com.gnoemes.shikimori.data.local.preference.impl

import android.content.SharedPreferences
import com.gnoemes.shikimori.data.local.preference.RateSortSource
import com.gnoemes.shikimori.di.app.annotations.SettingsQualifier
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.utils.putInt
import javax.inject.Inject

class RateSortSourceImpl @Inject constructor(
        @SettingsQualifier private val prefs: SharedPreferences
) : RateSortSource {

    companion object {
        private const val KEY_PART = "rate_sort"
    }

    override fun getSort(type: Type): RateSort {
        val order = prefs.getInt(getKey(type), 0)
        return RateSort.values().first { it.order == order }
    }

    override fun saveSort(type: Type, sort: RateSort) {
        prefs.putInt(getKey(type), sort.order)
    }

    private fun getKey(type: Type): String = KEY_PART + "_${type.name}"
}
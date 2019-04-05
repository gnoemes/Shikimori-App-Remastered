package com.gnoemes.shikimori.data.local.preference.impl

import android.content.SharedPreferences
import com.gnoemes.shikimori.data.local.preference.RateSortSource
import com.gnoemes.shikimori.di.app.annotations.SettingsQualifier
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.utils.putBoolean
import com.gnoemes.shikimori.utils.putInt
import javax.inject.Inject

class RateSortSourceImpl @Inject constructor(
        @SettingsQualifier private val prefs: SharedPreferences
) : RateSortSource {

    companion object {
        private const val SORT_KEY_PART = "rate_sort"
        private const val ORDER_KEY_PART = "rate_order"
    }

    override fun getSort(type: Type): RateSort {
        val order = prefs.getInt(getSortKey(type), 0)
        return RateSort.values().first { it.order == order }
    }
    override fun saveSort(type: Type, sort: RateSort) = prefs.putInt(getSortKey(type), sort.order)

    override fun getOrder(type: Type): Boolean = prefs.getBoolean(getOrderKey(type), false)
    override fun saveOrder(type: Type, desc: Boolean) = prefs.putBoolean(getOrderKey(type), desc)

    private fun getSortKey(type: Type): String = SORT_KEY_PART + "_${type.name}"
    private fun getOrderKey(type: Type): String = ORDER_KEY_PART + "_${type.name}"
}
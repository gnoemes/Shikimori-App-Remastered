package com.gnoemes.shikimori.data.local.preference

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.RateSort

interface RateSortSource {

    fun getSort(type: Type): RateSort

    fun saveSort(type: Type, sort: RateSort)
}
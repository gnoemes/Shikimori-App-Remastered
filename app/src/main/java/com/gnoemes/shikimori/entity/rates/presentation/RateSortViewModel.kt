package com.gnoemes.shikimori.entity.rates.presentation

import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class RateSortViewModel(
        val status : RateStatus,
        val currentSort : RateSort,
        val sorts : List<Pair<RateSort, String>>,
        val descending : Boolean,
        val isAnime : Boolean
)
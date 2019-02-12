package com.gnoemes.shikimori.entity.user.presentation

import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class UserRateViewModel(
        val isAnime: Boolean,
        val rates: Map<RateProgressStatus, Int>,
        val rawRates : Map<RateStatus, String>
)
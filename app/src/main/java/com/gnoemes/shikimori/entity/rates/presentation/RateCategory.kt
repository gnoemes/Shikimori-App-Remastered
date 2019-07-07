package com.gnoemes.shikimori.entity.rates.presentation

import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class RateCategory(
        val status : RateStatus,
        val localizedCategory : String,
        val count : Int
)
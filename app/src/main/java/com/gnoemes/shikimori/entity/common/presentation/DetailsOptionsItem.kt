package com.gnoemes.shikimori.entity.common.presentation

import com.gnoemes.shikimori.entity.rates.domain.RateStatus

data class DetailsOptionsItem(
        val rateStatus: RateStatus?,
        val isAnime: Boolean,
        val isGuest: Boolean,
        val watchOnlineText: String,
        val chronologyText: String
)
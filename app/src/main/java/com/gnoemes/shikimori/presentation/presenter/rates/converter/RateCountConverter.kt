package com.gnoemes.shikimori.presentation.presenter.rates.converter

import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.entity.user.domain.UserDetails

interface RateCountConverter {

    fun countAnimeRates(t: UserDetails): List<RateCategory>

    fun countMangaRates(t: UserDetails): List<RateCategory>
}
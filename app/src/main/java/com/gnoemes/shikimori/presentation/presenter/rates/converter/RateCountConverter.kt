package com.gnoemes.shikimori.presentation.presenter.rates.converter

import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.user.domain.UserDetails

interface RateCountConverter {

    fun countAnimeRates(t: UserDetails): List<Pair<RateStatus, String>>

    fun countMangaRates(t: UserDetails): List<Pair<RateStatus, String>>
}
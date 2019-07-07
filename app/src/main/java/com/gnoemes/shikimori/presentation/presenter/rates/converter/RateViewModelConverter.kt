package com.gnoemes.shikimori.presentation.presenter.rates.converter

import com.gnoemes.shikimori.entity.rates.domain.PinnedRate

interface RateViewModelConverter {

    fun apply(t: List<Any>, pinned : List<PinnedRate>): List<Any>
}
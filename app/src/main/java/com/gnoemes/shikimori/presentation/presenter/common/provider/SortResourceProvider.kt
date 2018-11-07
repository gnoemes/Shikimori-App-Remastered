package com.gnoemes.shikimori.presentation.presenter.common.provider

import com.gnoemes.shikimori.entity.common.presentation.RateSort

interface SortResourceProvider {

    fun getAnimeRateSorts() : List<Pair<RateSort, String>>

    fun getMangaRateSorts(): List<Pair<RateSort, String>>
}
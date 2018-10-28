package com.gnoemes.shikimori.presentation.presenter.rates.provider

interface RateResourceProvider {

    val animeRatesWithCount: List<String>

    val mangaRatesWithCount: List<String>
}
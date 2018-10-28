package com.gnoemes.shikimori.presentation.presenter.rates.provider

import android.content.Context
import com.gnoemes.shikimori.R
import javax.inject.Inject

class RateResourceProviderImpl @Inject constructor(
        private val context: Context
) : RateResourceProvider {

    override val animeRatesWithCount: List<String>
        get() = context.resources.getStringArray(R.array.anime_rate_stasuses_with_count).asList()

    override val mangaRatesWithCount: List<String>
        get() = context.resources.getStringArray(R.array.manga_rate_stasuses_with_count).asList()
}
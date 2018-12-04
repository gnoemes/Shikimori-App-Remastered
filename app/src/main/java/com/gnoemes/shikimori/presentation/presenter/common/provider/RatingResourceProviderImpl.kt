package com.gnoemes.shikimori.presentation.presenter.common.provider

import android.content.Context
import com.gnoemes.shikimori.R
import javax.inject.Inject

class RatingResourceProviderImpl @Inject constructor(
        private val context: Context
) : RatingResourceProvider {

    override fun getRatingDescription(rating: Int): String {
        return when (rating) {
            1 -> context.getString(R.string.rating_bad_ass)
            2 -> context.getString(R.string.rating_awful)
            3 -> context.getString(R.string.rating_very_bad)
            4 -> context.getString(R.string.rating_bad)
            5 -> context.getString(R.string.rating_not_bad)
            6 -> context.getString(R.string.rating_normal)
            7 -> context.getString(R.string.rating_good)
            8 -> context.getString(R.string.rating_fine)
            9 -> context.getString(R.string.rating_nuts)
            10 -> context.getString(R.string.rating_perfect)
            else -> context.getString(R.string.rating_empty)
        }
    }
}
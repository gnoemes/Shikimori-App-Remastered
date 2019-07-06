package com.gnoemes.shikimori.presentation.presenter.common.provider

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import javax.inject.Inject

class SortResourceProviderImpl @Inject constructor(
        val context: Context
) : SortResourceProvider {

    override fun getAnimeRateSorts(): List<Pair<RateSort, String>> =
            listOf(
                    Pair(RateSort.Name, context.getString(R.string.sort_name)),
                    Pair(RateSort.Progress, context.getString(R.string.sort_episodes_watched)),
                    Pair(RateSort.DateAired, context.getString(R.string.sort_date)),
                    Pair(RateSort.Score, context.getString(R.string.sort_score)),
                    Pair(RateSort.Episodes, context.getString(R.string.sort_episodes))
            )

    override fun getMangaRateSorts(): List<Pair<RateSort, String>> =
            listOf(
                    Pair(RateSort.Name, context.getString(R.string.sort_name)),
                    Pair(RateSort.Progress, context.getString(R.string.sort_chapters_readed)),
                    Pair(RateSort.DateAired, context.getString(R.string.sort_date)),
                    Pair(RateSort.Score, context.getString(R.string.sort_score)),
                    Pair(RateSort.Episodes, context.getString(R.string.sort_chapters))
            )
}
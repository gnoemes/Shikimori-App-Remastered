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
                    Pair(RateSort.Id, context.getString(R.string.sort_id)),
                    Pair(RateSort.Type, context.getString(R.string.sort_type)),
                    Pair(RateSort.DateAired, context.getString(R.string.sort_date)),
                    Pair(RateSort.Status, context.getString(R.string.sort_status)),
                    Pair(RateSort.Episodes, context.getString(R.string.sort_episodes)),
                    Pair(RateSort.EpisodesWatched, context.getString(R.string.sort_episodes_watched)),
                    Pair(RateSort.Score, context.getString(R.string.sort_score)),
                    Pair(RateSort.Random, context.getString(R.string.sort_random))
            )

    override fun getMangaRateSorts(): List<Pair<RateSort, String>> =
            listOf(
                    Pair(RateSort.Id, context.getString(R.string.sort_id)),
                    Pair(RateSort.Type, context.getString(R.string.sort_type)),
                    Pair(RateSort.DateAired, context.getString(R.string.sort_date)),
                    Pair(RateSort.Status, context.getString(R.string.sort_status)),
                    Pair(RateSort.Episodes, context.getString(R.string.sort_chapters)),
                    Pair(RateSort.EpisodesWatched, context.getString(R.string.sort_chapters_readed)),
                    Pair(RateSort.Score, context.getString(R.string.sort_score)),
                    Pair(RateSort.Random, context.getString(R.string.sort_random))
            )
}
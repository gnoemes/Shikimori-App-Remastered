package com.gnoemes.shikimori.presentation.presenter.rates.converter

import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.presentation.RateCategory
import com.gnoemes.shikimori.entity.user.domain.Status
import com.gnoemes.shikimori.entity.user.domain.UserDetails
import com.gnoemes.shikimori.presentation.presenter.rates.provider.RateResourceProvider
import javax.inject.Inject

class RateCountConverterImpl @Inject constructor(
        private val resourceProvider: RateResourceProvider
) : RateCountConverter {

    override fun countAnimeRates(t: UserDetails): List<RateCategory> {
        return if (!t.stats.hasAnime) emptyList()
        else {
            val statuses = t.stats.animeStatuses
            val formatStrings = resourceProvider.animeRatesWithCount
            countRates(statuses, formatStrings)
        }
    }

    override fun countMangaRates(t: UserDetails): List<RateCategory> {
        return if (!t.stats.hasManga) emptyList()
        else {
            val statuses = t.stats.mangaStatuses
            val formatStrings = resourceProvider.mangaRatesWithCount
            countRates(statuses, formatStrings)
        }
    }

    private fun countRates(statuses: List<Status>?, formatStrings: List<String>): List<RateCategory> {
        val watchingCount = countRateSize(statuses, RateStatus.WATCHING)
        val plannedCount = countRateSize(statuses, RateStatus.PLANNED)
        val rewatchingCount = countRateSize(statuses, RateStatus.REWATCHING)
        val watchedCount = countRateSize(statuses, RateStatus.COMPLETED)
        val onHoldCount = countRateSize(statuses, RateStatus.ON_HOLD)
        val droppedCount = countRateSize(statuses, RateStatus.DROPPED)

        val counts = arrayOf(watchingCount, plannedCount,
                rewatchingCount, watchedCount,
                onHoldCount, droppedCount)

        return RateStatus.values()
                .zip(formatStrings)
                .mapIndexedNotNull { index, pair ->
                    val count = counts[index]
                    if (count > 0) RateCategory(pair.first, pair.second, count)
                    else null
                }
    }

    private fun countRateSize(statuses: List<Status>?, rateStatus: RateStatus): Int {
        return statuses?.asSequence()?.filter { it.name == rateStatus }?.sumBy { it.size }
                ?: 0
    }

}
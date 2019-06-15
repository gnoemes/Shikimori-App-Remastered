package com.gnoemes.shikimori.presentation.presenter.rates.provider

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import javax.inject.Inject

class RateResourceProviderImpl @Inject constructor(
        private val context: Context
) : RateResourceProvider {

    override val animeRatesWithCount: List<String>
        get() = context.resources.getStringArray(R.array.anime_rate_stasuses).asList()

    override val mangaRatesWithCount: List<String>
        get() = context.resources.getStringArray(R.array.manga_rate_stasuses).asList()

    override fun getChangeRateStatusMessage(type: Type, status: RateStatus): String = when (type) {
        Type.ANIME -> getAnimeStatus(status)
        Type.MANGA -> getMangaStatus(status)
        Type.RANOBE -> getRanobeStatus(status)
        else -> ""
    }

    override fun getDeleteRateMessage(type: Type): String = when(type) {
        Type.ANIME -> context.getString(R.string.rate_message_anime_deleted)
        Type.MANGA -> context.getString(R.string.rate_message_manga_deleted)
        Type.RANOBE -> context.getString(R.string.rate_message_ranobe_deleted)
        else -> ""
    }

    private fun getAnimeStatus(status: RateStatus): String = when (status) {
        RateStatus.WATCHING -> context.getString(R.string.rate_message_anime_in_progress)
        RateStatus.PLANNED -> context.getString(R.string.rate_message_anime_planned)
        RateStatus.REWATCHING -> context.getString(R.string.rate_message_anime_rewatched)
        RateStatus.COMPLETED -> context.getString(R.string.rate_message_anime_completed)
        RateStatus.ON_HOLD -> context.getString(R.string.rate_message_anime_on_hold)
        RateStatus.DROPPED -> context.getString(R.string.rate_message_anime_drop)
    }

    private fun getMangaStatus(status: RateStatus): String = when (status) {
        RateStatus.WATCHING -> context.getString(R.string.rate_message_manga_in_progress)
        RateStatus.PLANNED -> context.getString(R.string.rate_message_manga_planned)
        RateStatus.REWATCHING -> context.getString(R.string.rate_message_manga_rewatched)
        RateStatus.COMPLETED -> context.getString(R.string.rate_message_manga_completed)
        RateStatus.ON_HOLD -> context.getString(R.string.rate_message_manga_on_hold)
        RateStatus.DROPPED -> context.getString(R.string.rate_message_manga_drop)
    }

    private fun getRanobeStatus(status: RateStatus): String = when (status) {
        RateStatus.WATCHING -> context.getString(R.string.rate_message_ranobe_in_progress)
        RateStatus.PLANNED -> context.getString(R.string.rate_message_ranobe_planned)
        RateStatus.REWATCHING -> context.getString(R.string.rate_message_ranobe_rewatched)
        RateStatus.COMPLETED -> context.getString(R.string.rate_message_ranobe_completed)
        RateStatus.ON_HOLD -> context.getString(R.string.rate_message_ranobe_on_hold)
        RateStatus.DROPPED -> context.getString(R.string.rate_message_ranobe_drop)
    }
}
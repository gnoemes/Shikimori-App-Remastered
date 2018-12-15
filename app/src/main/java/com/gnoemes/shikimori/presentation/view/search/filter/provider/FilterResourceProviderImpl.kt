package com.gnoemes.shikimori.presentation.view.search.filter.provider

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.AgeRating
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import java.util.*

class FilterResourceProviderImpl(
        private val context: Context,
        private val converter: FilterResourceConverter
) : FilterResourceProvider {

    override fun getAnimeFilters(): HashMap<String, FilterCategory> {
        val filters = HashMap<String, FilterCategory>()

        filters[SearchConstants.GENRE] = FilterCategory(getGenreString(), converter.convertAnimeFilters(getList(R.array.genres), getList(R.array.genres_names), Genre.values()))
        filters[SearchConstants.STATUS] = FilterCategory(getStatusString(), converter.convertAnimeFilters(getList(R.array.statuses), getList(Status.values()), Status.values()))
        filters[SearchConstants.TYPE] = FilterCategory(getTypeString(), converter.convertAnimeFilters(getList(R.array.anime_types), getList(R.array.anime_types_name), AnimeType.values()))
        filters[SearchConstants.DURATION] = FilterCategory(getDurationString(), converter.convertAnimeFilters(getList(R.array.duration), getList(R.array.duration_names), SearchConstants.DURATIONS.values()))
        filters[SearchConstants.RATE] = FilterCategory(getRateStatusString(), converter.convertAnimeFilters(getList(R.array.anime_rate_stasuses), getList(RateStatus.values()), RateStatus.values()))
        filters[SearchConstants.AGE_RATING] = FilterCategory(getAgeRatingString(), converter.convertAnimeFilters(getList(R.array.age_ratings), getList(AgeRating.values()), AgeRating.values()).apply { removeAt(lastIndex) })

        return filters
    }

    override fun getMangaFilters(): HashMap<String,FilterCategory> {
        val filters = HashMap<String, FilterCategory>()

        filters[SearchConstants.GENRE] = FilterCategory(getGenreString(), converter.convertMangaFilters(getList(R.array.genres), getList(R.array.genres_names), Genre.values()))
        filters[SearchConstants.STATUS] = FilterCategory(getStatusString(), converter.convertMangaFilters(getList(R.array.statuses), getList(R.array.statuses_names), Status.values()))
        filters[SearchConstants.TYPE] = FilterCategory(getTypeString(), converter.convertMangaFilters(getList(R.array.manga_types), getList(R.array.manga_types_name), MangaType.values()))
        filters[SearchConstants.RATE] = FilterCategory(getRateStatusString(), converter.convertMangaFilters(getList(R.array.manga_rate_stasuses), getList(RateStatus.values()), RateStatus.values()))

        return filters
    }

    override fun getRanobeFilters(): HashMap<String, FilterCategory> {
        val filters = HashMap<String, FilterCategory>()

        filters[SearchConstants.GENRE] = FilterCategory(getGenreString(), converter.convertMangaFilters(getList(R.array.genres), getList(R.array.genres_names), Genre.values()))
        filters[SearchConstants.STATUS] = FilterCategory(getStatusString(), converter.convertMangaFilters(getList(R.array.statuses), getList(R.array.statuses_names), Status.values()))
        filters[SearchConstants.RATE] = FilterCategory(getRateStatusString(), converter.convertMangaFilters(getList(R.array.manga_rate_stasuses), getList(RateStatus.values()), RateStatus.values()))

        return filters
    }

    private fun getDurationString(): String = context.getString(R.string.common_duration)
    private fun getStatusString(): String = context.getString(R.string.common_status)
    private fun getTypeString(): String = context.getString(R.string.common_type)
    private fun getGenreString(): String = context.getString(R.string.common_genre)
    private fun getRateStatusString(): String = context.getString(R.string.rate_status)
    private fun getAgeRatingString(): String = context.getString(R.string.common_age_rating)

    private fun getList(arrayRes: Int): MutableList<String> {
        return context.resources.getStringArray(arrayRes).toMutableList()
    }

    private fun getList(values: Array<RateStatus>): MutableList<String> {
        return values.map { it.status.toLowerCase() }.toMutableList()
    }

    private fun getList(values: Array<Status>): MutableList<String> {
        return values.map { it.status.toLowerCase() }.toMutableList()
    }

    private fun getList(values: Array<AgeRating>): MutableList<String> {
        return values.asSequence().filter { it.rating != AgeRating.NONE.rating }.map { it.rating.toLowerCase() }.toMutableList()
    }

}
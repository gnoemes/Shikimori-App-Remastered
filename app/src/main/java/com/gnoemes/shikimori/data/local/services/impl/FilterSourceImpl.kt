package com.gnoemes.shikimori.data.local.services.impl

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.services.FilterSource
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import org.joda.time.DateTime
import javax.inject.Inject

class FilterSourceImpl @Inject constructor(
        private val context: Context
) : FilterSource {

    override fun getAnimeFilters(): List<FilterCategory> = listOf(
            FilterCategory(FilterType.GENRE, getGenreString(), getFilters(FilterType.GENRE, true)),
            FilterCategory(FilterType.SEASON, getSeasonString(), getFilters(FilterType.SEASON)),
            FilterCategory(FilterType.STATUS, getStatusString(), getFilters(FilterType.STATUS)),
            FilterCategory(FilterType.RATE, getRateStatusString(), getFilters(FilterType.RATE, true)),
            FilterCategory(FilterType.KIND, getTypeString(), getFilters(FilterType.KIND, true)),
            FilterCategory(FilterType.DURATION, getDurationString(), getFilters(FilterType.DURATION)),
            FilterCategory(FilterType.AGE_RATING, getAgeRatingString(), getFilters(FilterType.AGE_RATING))
    )

    override fun getMangaFilters(): List<FilterCategory> = listOf(
            FilterCategory(FilterType.GENRE, getGenreString(), getFilters(FilterType.GENRE)),
            FilterCategory(FilterType.SEASON, getSeasonString(), getFilters(FilterType.SEASON)),
            FilterCategory(FilterType.STATUS, getStatusString(), getFilters(FilterType.STATUS)),
            FilterCategory(FilterType.RATE, getRateStatusString(), getFilters(FilterType.RATE)),
            FilterCategory(FilterType.KIND, getTypeString(), getFilters(FilterType.KIND))
    )

    override fun getRanobeFilters(): List<FilterCategory> = listOf(
            FilterCategory(FilterType.GENRE, getGenreString(), getFilters(FilterType.GENRE)),
            FilterCategory(FilterType.SEASON, getSeasonString(), getFilters(FilterType.SEASON)),
            FilterCategory(FilterType.STATUS, getStatusString(), getFilters(FilterType.STATUS)),
            FilterCategory(FilterType.RATE, getRateStatusString(), getFilters(FilterType.RATE))
    )

    private fun getFilters(type: FilterType, isAnime: Boolean = false): MutableList<FilterItem> {
        return when (type) {
            FilterType.GENRE -> getGenres(isAnime)
            FilterType.SEASON -> getSeasons()
            FilterType.STATUS -> getStatuses()
            FilterType.RATE -> getRates(isAnime)
            FilterType.KIND -> getKinds(isAnime)
            FilterType.DURATION -> getDurations()
            FilterType.AGE_RATING -> getAgeRatings()
            else -> mutableListOf()
        }
    }

    private fun getAgeRatings(): MutableList<FilterItem> =
            getList(R.array.age_ratings)
                    .zip(AgeRating.values().asSequence().filter { it.rating != AgeRating.NONE.rating }.map { it.rating.toLowerCase() }.toMutableList())
                    .map { (name, value) -> convert(FilterType.AGE_RATING.value, value, name) }
                    .toMutableList()
                    .apply { removeAt(lastIndex) }

    private fun getDurations(): MutableList<FilterItem> =
            getList(R.array.duration)
                    .zip(getList(R.array.duration_names))
                    .map { (name, value) -> convert(FilterType.DURATION.value, value, name) }
                    .toMutableList()

    private fun getKinds(anime: Boolean): MutableList<FilterItem> =
            getList(if (anime) R.array.anime_types else R.array.manga_types)
                    .zip(getList(if (anime) R.array.anime_types_name else R.array.manga_types_name))
                    .asSequence()
                    .mapNotNull { pair ->
                        if (anime) AnimeType.values().find { it.type == pair.second }?.let { Pair(pair.first, it.type) }
                        else MangaType.values().find { it.type == pair.second }?.let { Pair(pair.first, it.type) }
                    }
                    .map { (name, value) -> convert(FilterType.KIND.value, value, name) }
                    .toMutableList()


    private fun getRates(anime: Boolean): MutableList<FilterItem> =
            getList(if (anime) R.array.anime_rate_stasuses else R.array.manga_rate_stasuses)
                    .zip(RateStatus.values().map { it.status.toLowerCase() }.toMutableList())
                    .map { (name, value) -> convert(FilterType.RATE.value, value, name) }
                    .toMutableList()

    private fun getStatuses(): MutableList<FilterItem> =
            getList(R.array.filter_statuses)
                    .zip(Status.values().map { it.status.toLowerCase() }.toMutableList().apply { set(lastIndex, "latest") })
                    .map { (name, value) -> convert(FilterType.STATUS.value, value, name) }
                    .toMutableList()

    private fun getGenres(anime: Boolean): MutableList<FilterItem> =
            getList(R.array.genres)
                    .zip(getList(R.array.genres_names))
                    .asSequence()
                    .mapNotNull { pair -> Genre.values().find { it.equalsName(pair.second) }?.let { Pair(pair.first, if (anime) it.animeId else it.mangaId) } }
                    .map { (name, value) -> convert(FilterType.GENRE.value, value, name) }
                    .toMutableList()

    private fun convert(action: String, value: String?, text: String?) = FilterItem(action, value, text)

    private fun getDurationString(): String = context.getString(R.string.common_duration)
    private fun getStatusString(): String = context.getString(R.string.common_status)
    private fun getTypeString(): String = context.getString(R.string.common_type)
    private fun getGenreString(): String = context.getString(R.string.common_genre)
    private fun getRateStatusString(): String = context.getString(R.string.rate_status)
    private fun getAgeRatingString(): String = context.getString(R.string.common_age_rating)
    private fun getSeasonString(): String = context.getString(R.string.common_season)
    private fun getList(arrayRes: Int): MutableList<String> = context.resources.getStringArray(arrayRes).toMutableList()

    private fun getSeasons(): MutableList<FilterItem> {
        val seasons = getList(R.array.seasons).zip(SearchConstants.SEASONS.values())

        val season = getCurrentSeason()
        val nextSeason = getNextSeason(season)
        val prevSeason = getPrevSeason(season)

        val seasonQue = listOf(nextSeason, season, prevSeason, getPrevSeason(prevSeason))

        fun getSeasonValue(season: SearchConstants.SEASONS, year: Int): String {
            return "${season}_$year"
        }

        fun getSeasonLocalization(season: SearchConstants.SEASONS, year: Int): String? {
            return seasons.find { it.second == season }?.first?.plus(" $year")
        }

        fun getSeasonFilter(season: SearchConstants.SEASONS, year: Int): FilterItem {
            return FilterItem(SearchConstants.SEASON, getSeasonValue(season, year), getSeasonLocalization(season, year))
        }

        fun getYearFilter(year: Int): FilterItem {
            return FilterItem(SearchConstants.SEASON, "$year", "$year ${context.getString(R.string.common_year).toLowerCase()}")
        }

        fun getYearFilter(year: IntRange): FilterItem {
            return FilterItem(SearchConstants.SEASON, "${year.first}_${year.last}", "${year.first} - ${year.last}")
        }

        return mutableListOf<FilterItem>()
                .apply {
                    //last seasons
                    add(getSeasonFilter(nextSeason, getSeasonYear(seasonQue, nextSeason)))
                    add(getSeasonFilter(season, getSeasonYear(seasonQue, season)))
                    add(getSeasonFilter(prevSeason, getSeasonYear(seasonQue, prevSeason)))
                    add(getSeasonFilter(getPrevSeason(prevSeason), getSeasonYear(seasonQue, getPrevSeason(prevSeason))))

                    //last two years
                    add(getYearFilter(getSeasonYear(seasonQue, nextSeason)))
                    val lastYearSingle = getSeasonYear(seasonQue, nextSeason) - 1
                    add(getYearFilter(lastYearSingle))

                    //Ranges
                    add(getYearFilter(IntRange(2016, lastYearSingle - 1)))
                    add(getYearFilter(2011..2015))
                    add(getYearFilter(2000..2010))

                    //older
                    add(FilterItem(SearchConstants.SEASON, "1950_1999", context.getString(R.string.filter_older)))
                }
    }

    private fun getCurrentYear(): Int {
        return DateTime.now().year
    }

    private fun getCurrentMonth(): Int {
        return DateTime.now().monthOfYear + 1
    }

    private fun getSeasonYear(seasonQue: List<SearchConstants.SEASONS>, season: SearchConstants.SEASONS): Int {
        val year = getCurrentYear()

        val seasonIndex = seasonQue.indexOf(season)
        val winterIndex = seasonQue.indexOf(SearchConstants.SEASONS.WINTER)

        return when {
            (seasonIndex == 0 && season == SearchConstants.SEASONS.WINTER) || (season == SearchConstants.SEASONS.WINTER && getCurrentMonth() == 12) -> year + 1
            winterIndex != 0 && seasonIndex > winterIndex -> year - 1
            else -> year
        }
    }

    private fun getCurrentSeason(): SearchConstants.SEASONS {
        val month = getCurrentMonth()
        return when (month) {
            12, in 1..2 -> SearchConstants.SEASONS.WINTER
            in 3..5 -> SearchConstants.SEASONS.SPRING
            in 6..8 -> SearchConstants.SEASONS.SUMMER
            else -> SearchConstants.SEASONS.FALL
        }
    }

    private fun getNextSeason(season: SearchConstants.SEASONS): SearchConstants.SEASONS {
        return when (season) {
            SearchConstants.SEASONS.FALL -> SearchConstants.SEASONS.WINTER
            SearchConstants.SEASONS.WINTER -> SearchConstants.SEASONS.SPRING
            SearchConstants.SEASONS.SPRING -> SearchConstants.SEASONS.SUMMER
            else -> SearchConstants.SEASONS.FALL
        }
    }

    private fun getPrevSeason(season: SearchConstants.SEASONS): SearchConstants.SEASONS {
        return when (season) {
            SearchConstants.SEASONS.WINTER -> SearchConstants.SEASONS.FALL
            SearchConstants.SEASONS.SPRING -> SearchConstants.SEASONS.WINTER
            SearchConstants.SEASONS.SUMMER -> SearchConstants.SEASONS.SPRING
            else -> SearchConstants.SEASONS.SUMMER
        }
    }

}
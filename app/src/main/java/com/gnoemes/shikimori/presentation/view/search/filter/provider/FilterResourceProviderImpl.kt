package com.gnoemes.shikimori.presentation.view.search.filter.provider

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.*
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import org.joda.time.DateTime
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
        filters[SearchConstants.SEASON] = FilterCategory(getSeasonString(), getSeasonFilters())

        return filters
    }

    override fun getMangaFilters(): HashMap<String, FilterCategory> {
        val filters = HashMap<String, FilterCategory>()

        filters[SearchConstants.GENRE] = FilterCategory(getGenreString(), converter.convertMangaFilters(getList(R.array.genres), getList(R.array.genres_names), Genre.values()))
        filters[SearchConstants.STATUS] = FilterCategory(getStatusString(), converter.convertMangaFilters(getList(R.array.statuses), getList(R.array.statuses_names), Status.values()))
        filters[SearchConstants.TYPE] = FilterCategory(getTypeString(), converter.convertMangaFilters(getList(R.array.manga_types), getList(R.array.manga_types_name), MangaType.values()))
        filters[SearchConstants.RATE] = FilterCategory(getRateStatusString(), converter.convertMangaFilters(getList(R.array.manga_rate_stasuses), getList(RateStatus.values()), RateStatus.values()))
        filters[SearchConstants.SEASON] = FilterCategory(getSeasonString(), getSeasonFilters())

        return filters
    }

    override fun getRanobeFilters(): HashMap<String, FilterCategory> {
        val filters = HashMap<String, FilterCategory>()

        filters[SearchConstants.GENRE] = FilterCategory(getGenreString(), converter.convertMangaFilters(getList(R.array.genres), getList(R.array.genres_names), Genre.values()))
        filters[SearchConstants.STATUS] = FilterCategory(getStatusString(), converter.convertMangaFilters(getList(R.array.statuses), getList(R.array.statuses_names), Status.values()))
        filters[SearchConstants.RATE] = FilterCategory(getRateStatusString(), converter.convertMangaFilters(getList(R.array.manga_rate_stasuses), getList(RateStatus.values()), RateStatus.values()))
        filters[SearchConstants.SEASON] = FilterCategory(getSeasonString(), getSeasonFilters())

        return filters
    }

    private fun getDurationString(): String = context.getString(R.string.common_duration)
    private fun getStatusString(): String = context.getString(R.string.common_status)
    private fun getTypeString(): String = context.getString(R.string.common_type)
    private fun getGenreString(): String = context.getString(R.string.common_genre)
    private fun getRateStatusString(): String = context.getString(R.string.rate_status)
    private fun getAgeRatingString(): String = context.getString(R.string.common_age_rating)
    private fun getSeasonString(): String = context.getString(R.string.common_season)

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


    private fun getSeasonFilters(): MutableList<FilterItem> {
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
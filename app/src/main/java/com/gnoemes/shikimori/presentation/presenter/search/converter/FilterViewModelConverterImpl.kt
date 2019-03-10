package com.gnoemes.shikimori.presentation.presenter.search.converter

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.*
import com.gnoemes.shikimori.utils.exist
import javax.inject.Inject

class FilterViewModelConverterImpl @Inject constructor() : FilterViewModelConverter {

    override fun convert(filters: List<FilterCategory>, appliedFilters: HashMap<String, MutableList<FilterItem>>): List<Any> {
        val items = mutableListOf<Any>()
        filters.forEach { items.add(convertCategory(it, appliedFilters)) }

        return items
    }

    private fun convertCategory(category: FilterCategory, appliedFilters: HashMap<String, MutableList<FilterItem>>): Any {
        val applied = appliedFilters[category.filterType.value]

        val items = category.filters.map {
            val statuses = getAppliedStatus(it, applied)
            convertFilter(it, statuses.first, statuses.second)
        }

        return when (category.filterType) {
            FilterType.GENRE -> FilterNestedViewModel(category.categoryLocalized, category.filterType, items, applied.size())
            FilterType.SEASON -> FilterNestedViewModel(category.categoryLocalized, category.filterType, items, applied.size())
            FilterType.RATE -> FilterWithButtonsViewModel(category.categoryLocalized, category.filterType, items, hasDelete = true, hasInvert = true, hasSelectAll = true)
            FilterType.KIND -> FilterWithButtonsViewModel(category.categoryLocalized, category.filterType, items, hasDelete = true, hasInvert = false, hasSelectAll = false)
            else -> FilterWithButtonsViewModel(category.categoryLocalized, category.filterType, items, hasDelete = false, hasInvert = false, hasSelectAll = false)
        }
    }

    override fun convertGenres(category: FilterCategory, appliedFilters: HashMap<String, MutableList<FilterItem>>): List<Any> {
        val items = mutableListOf<Any>()

        val mainGenres = mutableListOf(
                Genre.SHOUNEN, Genre.SHOUNEN_AI, Genre.SEINEN,
                Genre.SHOUJO, Genre.SHOUJO_AI, Genre.JOSEI,
                Genre.COMEDY, Genre.ROMANCE, Genre.SCHOOL
        )

        val applied = appliedFilters[category.filterType.value]

        val mainCategoryFilters = category.filters
                .asSequence()
                .filter {
                    mainGenres.exist { genre -> genre.animeId == it.value || genre.mangaId == it.value }
                }
                .map {
                    val statuses = getAppliedStatus(it, applied)
                    convertFilter(it, statuses.first, statuses.second)
                }
                .toMutableList()

        items.add(FilterMainGenreCategory(mainCategoryFilters))


        val otherCategoryFilters = category.filters
                .asSequence()
                .filter {
                    !mainGenres.exist { genre -> genre.animeId == it.value || genre.mangaId == it.value }
                }
                .sortedBy { it.localizedText }
                .map {
                    val statuses = getAppliedStatus(it, applied)
                    convertFilter(it, statuses.first, statuses.second)
                }
                .groupBy { it.text.first().toUpperCase() }
                .entries
                .map { FilterGenreItem(it.key.toString(), it.value) }
                .toMutableList()

        items.add(FilterOtherGenreCategory(otherCategoryFilters))

        return items
    }

    override fun convertSeasons(category: FilterCategory, appliedFilters: HashMap<String, MutableList<FilterItem>>): List<FilterViewModel> {
        val applied = appliedFilters[category.filterType.value]

     return category.filters
                .map {
                    val statuses = getAppliedStatus(it, applied)
                    convertFilter(it, statuses.first, statuses.second)
                }
    }

    private fun getAppliedStatus(checkItem: FilterItem, applied: MutableList<FilterItem>?): Pair<Boolean, Boolean> {
        val item = applied?.find { checkItem.value?.equals(it.value?.replace("!", ""))!! }
        return Pair(item != null, item != null && item.value!!.contains("!"))
    }

    private fun convertFilter(item: FilterItem, isApplied: Boolean, isInverted: Boolean): FilterViewModel {
        val state = when {
            isInverted -> FilterViewModel.STATE.INVERTED
            isApplied -> FilterViewModel.STATE.SELECTED
            else -> FilterViewModel.STATE.DEFAULT
        }
        val value =
                if (isInverted) "!${item.value}"
                else item.value!!

        return FilterViewModel(state, value, item.localizedText!!)
    }

    private fun List<Any>?.size(): Int = this?.size ?: 0
}
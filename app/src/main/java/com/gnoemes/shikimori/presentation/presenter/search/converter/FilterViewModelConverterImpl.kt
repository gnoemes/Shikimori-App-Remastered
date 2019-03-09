package com.gnoemes.shikimori.presentation.presenter.search.converter

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import com.gnoemes.shikimori.entity.search.presentation.FilterNestedViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterWithButtonsViewModel
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
package com.gnoemes.shikimori.presentation.presenter.search.converter

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel

interface FilterViewModelConverter  {

    fun convert(filters : List<FilterCategory>, appliedFilters : HashMap<String, MutableList<FilterItem>>) : List<Any>

    fun convertGenres(category: FilterCategory, appliedFilters: HashMap<String, MutableList<FilterItem>>): List<Any>

    fun convertSeasons(category: FilterCategory, appliedFilters: HashMap<String, MutableList<FilterItem>>): List<FilterViewModel>

    fun convertCustomSeasons(appliedFilters: HashMap<String, MutableList<FilterItem>>): List<Any>

}
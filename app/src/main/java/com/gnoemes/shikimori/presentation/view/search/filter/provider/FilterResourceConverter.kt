package com.gnoemes.shikimori.presentation.view.search.filter.provider

import com.gnoemes.shikimori.entity.common.domain.FilterItem

interface FilterResourceConverter {
    fun <T> convertMangaFilters(values: MutableList<String>, names: MutableList<String>, type: Array<T>): MutableList<FilterItem>

    fun <T> convertAnimeFilters(values: MutableList<String>, names: MutableList<String>, type: Array<T>): MutableList<FilterItem>
}
package com.gnoemes.shikimori.data.local.services

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory

interface FilterSource {

    fun getAnimeFilters(): List<FilterCategory>
    fun getMangaFilters(): List<FilterCategory>
    fun getRanobeFilters(): List<FilterCategory>

    fun getAnimeSortFilters() : List<FilterItem>
    fun getMangaSortFilters() : List<FilterItem>
}
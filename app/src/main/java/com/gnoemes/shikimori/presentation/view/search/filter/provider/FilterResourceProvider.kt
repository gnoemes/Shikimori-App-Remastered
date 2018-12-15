package com.gnoemes.shikimori.presentation.view.search.filter.provider

import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import java.util.*

interface FilterResourceProvider {

    fun getAnimeFilters(): HashMap<String, FilterCategory>

    fun getMangaFilters(): HashMap<String, FilterCategory>

    fun getRanobeFilters(): HashMap<String, FilterCategory>

}
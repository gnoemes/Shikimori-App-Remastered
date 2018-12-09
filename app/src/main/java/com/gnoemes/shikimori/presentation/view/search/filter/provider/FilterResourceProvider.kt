package com.gnoemes.shikimori.presentation.view.search.filter.provider

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import java.util.*

interface FilterResourceProvider {

    fun getAnimeFilters(): HashMap<String, Pair<String, MutableList<FilterItem>>>

    fun getMangaFilters(): HashMap<String, Pair<String, MutableList<FilterItem>>>

    fun getRanobeFilters(): HashMap<String, Pair<String, MutableList<FilterItem>>>

}
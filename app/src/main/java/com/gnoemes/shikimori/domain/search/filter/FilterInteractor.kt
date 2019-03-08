package com.gnoemes.shikimori.domain.search.filter

import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import io.reactivex.Single

interface FilterInteractor {
    fun getAnimeFilters(): Single<List<FilterCategory>>
    fun getMangaFilters(): Single<List<FilterCategory>>
    fun getRanobeFilters(): Single<List<FilterCategory>>
}
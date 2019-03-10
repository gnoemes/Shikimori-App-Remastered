package com.gnoemes.shikimori.data.repository.search

import com.gnoemes.shikimori.data.local.services.FilterSource
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import io.reactivex.Single
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
        private val source: FilterSource
) : FilterRepository {

    override fun getAnimeFilters(): Single<List<FilterCategory>> = Single.just(source.getAnimeFilters())
    override fun getMangaFilters(): Single<List<FilterCategory>> = Single.just(source.getMangaFilters())
    override fun getRanobeFilters(): Single<List<FilterCategory>> = Single.just(source.getRanobeFilters())

    override fun getAnimeSortFilters(): Single<List<FilterItem>> = Single.just(source.getAnimeSortFilters())
    override fun getMangaSortFilters(): Single<List<FilterItem>> = Single.just(source.getMangaSortFilters())
}
package com.gnoemes.shikimori.domain.search.filter

import com.gnoemes.shikimori.data.repository.search.FilterRepository
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class FilterInteractorImpl @Inject constructor(
        private val repository: FilterRepository
) : FilterInteractor {

    override fun getAnimeFilters(): Single<List<FilterCategory>> = repository.getAnimeFilters().applyErrorHandlerAndSchedulers()

    override fun getMangaFilters(): Single<List<FilterCategory>> = repository.getMangaFilters().applyErrorHandlerAndSchedulers()

    override fun getRanobeFilters(): Single<List<FilterCategory>> = repository.getRanobeFilters().applyErrorHandlerAndSchedulers()
}
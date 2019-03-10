package com.gnoemes.shikimori.presentation.presenter.search

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.search.filter.FilterInteractor
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.presentation.presenter.base.BaseFilterPresenter
import com.gnoemes.shikimori.presentation.presenter.search.converter.FilterViewModelConverter
import com.gnoemes.shikimori.presentation.view.search.filter.genres.FilterGenresView
import javax.inject.Inject

@InjectViewState
class FilterGenresPresenter @Inject constructor(
        private val interactor: FilterInteractor,
        private val converter: FilterViewModelConverter
) : BaseFilterPresenter<FilterGenresView>() {

    override fun loadData() =
            (when (type) {
                Type.MANGA -> interactor.getMangaFilters()
                Type.RANOBE -> interactor.getRanobeFilters()
                else -> interactor.getAnimeFilters()
            })
                    .map { list -> list.first { it.filterType == FilterType.GENRE } }
                    .map { converter.convertGenres(it, appliedFilters) }
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun setData(it : List<Any>) {
        viewState.showData(it)
    }

}
package com.gnoemes.shikimori.presentation.presenter.search

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.search.filter.FilterInteractor
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterEntryViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseFilterPresenter
import com.gnoemes.shikimori.presentation.presenter.search.converter.FilterViewModelConverter
import com.gnoemes.shikimori.presentation.view.search.filter.seasons.FilterSeasonsView
import javax.inject.Inject

@InjectViewState
class FilterSeasonsPresenter @Inject constructor(
        private val interactor: FilterInteractor,
        private val converter: FilterViewModelConverter
) : BaseFilterPresenter<FilterSeasonsView>() {

    override fun loadData() =
            (when (type) {
                Type.MANGA -> interactor.getMangaFilters()
                Type.RANOBE -> interactor.getRanobeFilters()
                else -> interactor.getAnimeFilters()
            })
                    .map { list -> list.first { it.filterType == FilterType.SEASON } }
                    .map { converter.convertSeasons(it, appliedFilters) }
                    .doOnSuccess { loadCustomFilters() }
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun loadCustomFilters() {
        val items = converter.convertCustomSeasons(appliedFilters)
        viewState.showCustomData(items)
    }

    private fun setData(it : List<FilterViewModel>) {
        viewState.showSimpleData(it)
    }

    fun onRemoveCustomFilter(item : FilterEntryViewModel) {
        removeFromSelected(FilterType.SEASON.value, FilterItem(FilterType.SEASON.value, item.value, ""))
        onFiltersChanged()
    }

    fun onNewCustomFilter(value : String) {
        addToSelected(FilterType.SEASON.value, FilterItem(FilterType.SEASON.value, value, ""))
        onFiltersChanged()
    }
}
package com.gnoemes.shikimori.presentation.presenter.search

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.search.filter.FilterInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterAction
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterWithButtonsViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseFilterPresenter
import com.gnoemes.shikimori.presentation.presenter.search.converter.FilterViewModelConverter
import com.gnoemes.shikimori.presentation.view.search.filter.FilterView
import com.gnoemes.shikimori.utils.clearAndAddAll
import javax.inject.Inject

@InjectViewState
open class FilterPresenter @Inject constructor(
        private val interactor: FilterInteractor,
        private val converter: FilterViewModelConverter
) : BaseFilterPresenter<FilterView>() {

    private val items = mutableListOf<Any>()

    private val sortItems = mutableListOf<FilterItem>()

    override fun initData() {
        loadSortFilters()
        onFiltersChanged()
    }

    private fun loadSortFilters() {
        (when (type) {
            Type.MANGA -> interactor.getMangaSortFilters()
            else -> interactor.getAnimeSortFilters()
        })
                .doOnSuccess { sortItems.clearAndAddAll(it) }
                .subscribe(this::setSortFilters, this::processErrors)
                .addToDisposables()
    }

    override fun loadData() {
        (when (type) {
            Type.MANGA -> interactor.getMangaFilters()
            Type.RANOBE -> interactor.getRanobeFilters()
            else -> interactor.getAnimeFilters()
        })
                .map { converter.convert(it, appliedFilters) }
                .subscribe(this::setData, this::processErrors)
                .addToDisposables()
    }

    private fun setData(it: List<Any>) {
        items.clearAndAddAll(it)
        viewState.showData(it)
    }

    private fun setSortFilters(it: List<FilterItem>) {
        val selectedPos = it.indexOfFirst { it.value == appliedFilters[SearchConstants.ORDER]?.firstOrNull()?.value }
        viewState.setSortFilters(it, if (selectedPos == -1) 1 else selectedPos)
    }

    fun onSortChanged(newSort: FilterItem) {
        clearAndAddToSelected(SearchConstants.ORDER, newSort)
    }

    fun onFilterAction(type: FilterType, action: FilterAction) {
        when (action) {
            is FilterAction.Clear -> clearSection(type.value)
            is FilterAction.Invert -> invertSection(type.value)
            is FilterAction.SelectAll -> selectSection(type.value)
            is FilterAction.ShowNested -> showNested(type)
        }
    }

    private fun showNested(type: FilterType) {
        when(type) {
            FilterType.GENRE -> {
                viewState.showGenresDialog(super.type, appliedFilters)
                logEvent(AnalyticEvent.FILTER_GENRES_OPENED)
            }
            FilterType.SEASON -> {
                viewState.showSeasonsDialog(super.type, appliedFilters)
                logEvent(AnalyticEvent.FILTER_SEASONS_OPENED)
            }
            else -> Unit
        }
    }

    override fun onResetClicked() {
        super.onResetClicked()
        loadSortFilters()
    }

    private fun clearAndAddToSelected(key: String, filterItem: FilterItem) {
        clearSection(key)
        addToSelected(key, filterItem)
    }

    private fun clearSection(key: String) {
        appliedFilters.remove(key)
        onFiltersChanged()
    }

    private fun selectSection(key: String) {
        appliedFilters[key] = items
                .asSequence()
                .filter { it is FilterWithButtonsViewModel }
                .map { it as FilterWithButtonsViewModel }
                .find { it.type.value == key }
                ?.let { it.filters }
                ?.map { FilterItem(key, it.value.replace("!", ""), it.text) }
                ?.toMutableList() ?: mutableListOf()
        onFiltersChanged()
    }

    private fun invertSection(key: String) {
        appliedFilters[key] = items
                .asSequence()
                .filter { it is FilterWithButtonsViewModel }
                .map { it as FilterWithButtonsViewModel }
                .find { it.type.value == key }
                ?.let { it.filters }
                ?.filter { it.state != FilterViewModel.STATE.DEFAULT }
                ?.map { FilterItem(key, it.value.invertValue(), it.text) }
                ?.toMutableList() ?: mutableListOf()
        onFiltersChanged()
    }

    fun onNestedFilterCallback(key: String?, newAppliedFilters: HashMap<String, MutableList<FilterItem>>) {
        if (newAppliedFilters.isEmpty()) clearSection(key!!)
        else appliedFilters.putAll(newAppliedFilters)
        onFiltersChanged()
    }
}
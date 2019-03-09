package com.gnoemes.shikimori.presentation.presenter.search

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.search.filter.FilterInteractor
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterAction
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterWithButtonsViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.search.converter.FilterViewModelConverter
import com.gnoemes.shikimori.presentation.view.search.filter.FilterView
import com.gnoemes.shikimori.utils.clearAndAddAll
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(
        private val interactor: FilterInteractor,
        private val converter: FilterViewModelConverter
) : BaseNetworkPresenter<FilterView>() {

    lateinit var type: Type
    lateinit var appliedFilters: HashMap<String, MutableList<FilterItem>>

    private val items = mutableListOf<Any>()

    override fun initData() {
        onFiltersChanged()
//        loadData()
    }

    private fun loadData() {
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

    private fun onFiltersChanged() {
        viewState.setResetEnabled(appliedFilters.size > 0)
        loadData()
    }

    fun onFilterAction(type: FilterType, action: FilterAction) {
        when (action) {
            is FilterAction.Clear -> clear(type.value)
            is FilterAction.Invert -> invert(type.value)
            is FilterAction.SelectAll -> select(type.value)
        }
    }

    fun onFilterInverted(type: FilterType, item: FilterViewModel) {
        when (item.state) {
            FilterViewModel.STATE.INVERTED -> Unit
            else -> invertOrAddNew(type.value, FilterItem(type.value, item.value, item.text))
        }
        onFiltersChanged()
    }

    fun onFilterSelected(type: FilterType, item: FilterViewModel) {
        when (item.state) {
            FilterViewModel.STATE.DEFAULT -> addToSelected(type.value, FilterItem(type.value, item.value, item.text))
            else -> removeFromSelected(type.value, FilterItem(type.value, item.value, item.text))
        }
        onFiltersChanged()
    }

    fun onResetClicked() {
        appliedFilters.clear()
        onFiltersChanged()
    }

    fun onAcceptClicked() {
        viewState.onFiltersAccepted(appliedFilters)
    }

    private fun clear(key: String) {
        appliedFilters.remove(key)
        onFiltersChanged()
    }

    private fun select(key: String) {
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

    private fun invert(key: String) {
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

    private fun getAppliedFilter(key: String, value: String?): FilterItem? {
        return appliedFilters[key]?.find { it.value == value }
    }

    private fun invertOrAddNew(key: String, value: FilterItem) {
        val item = getAppliedFilter(key, value.value)
        item?.let { removeFromSelected(key, it); addInverted(key, it) } ?: addInverted(key, value)
    }

    private fun addInverted(key: String, item: FilterItem) {
        val newItem =
                if (!item.value?.contains("!")!!) item.copy(value = "!${item.value}")
                else item
        addToSelected(key, newItem)
    }

    private fun addToSelected(key: String, value: FilterItem) {
        if (appliedFilters[key] != null && !appliedFilters[key]?.contains(value)!!) {
            appliedFilters[key]?.add(value)
        } else {
            appliedFilters[key] = mutableListOf(value)
        }
    }

    private fun removeFromSelected(key: String, value: FilterItem) {
        if (appliedFilters[key]?.size == 1) {
            appliedFilters.remove(key)
        } else {
            appliedFilters[key]?.remove(value)
        }
    }

    private fun String.invertValue(): String = if (this.contains("!")) this.replace("!", "") else "!${this}"

}
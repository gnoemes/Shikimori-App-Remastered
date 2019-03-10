package com.gnoemes.shikimori.presentation.presenter.base

import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFilterView

abstract class BaseFilterPresenter<View : BaseFilterView> : BaseNetworkPresenter<View>() {

    lateinit var type: Type
    lateinit var appliedFilters: HashMap<String, MutableList<FilterItem>>

    override fun initData() {
        onFiltersChanged()
    }

    abstract fun loadData()

    protected open fun onFiltersChanged() {
        viewState.setResetEnabled(appliedFilters.size > 0)
        loadData()
    }

    open fun onResetClicked() {
        appliedFilters.clear()
        onFiltersChanged()
    }

    open fun onAcceptClicked() {
        viewState.onFiltersAccepted(appliedFilters)
    }

    open fun onFilterInverted(type: FilterType, item: FilterViewModel) {
        when (item.state) {
            FilterViewModel.STATE.INVERTED -> Unit
            else -> invertOrAddNew(type.value, FilterItem(type.value, item.value, item.text))
        }
        onFiltersChanged()
    }

    open fun onFilterSelected(type: FilterType, item: FilterViewModel) {
        when (item.state) {
            FilterViewModel.STATE.DEFAULT -> addToSelected(type.value, FilterItem(type.value, item.value, item.text))
            else -> removeFromSelected(type.value, FilterItem(type.value, item.value, item.text))
        }
        onFiltersChanged()
    }

    protected open fun getAppliedFilter(key: String, value: String?): FilterItem? {
        return appliedFilters[key]?.find { it.value == value }
    }

    protected open fun invertOrAddNew(key: String, value: FilterItem) {
        val item = getAppliedFilter(key, value.value)
        item?.let { removeFromSelected(key, it); addInverted(key, it) } ?: addInverted(key, value)
    }

    protected open fun addInverted(key: String, item: FilterItem) {
        val newItem =
                if (!item.value?.contains("!")!!) item.copy(value = "!${item.value}")
                else item
        addToSelected(key, newItem)
    }

    protected open fun addToSelected(key: String, value: FilterItem) {
        if (appliedFilters[key] != null && !appliedFilters[key]?.contains(value)!!) {
            appliedFilters[key]?.add(value)
        } else {
            appliedFilters[key] = mutableListOf(value)
        }
    }

    protected open fun removeFromSelected(key: String, value: FilterItem) {
        if (appliedFilters[key]?.size == 1) {
            appliedFilters.remove(key)
        } else {
            appliedFilters[key]?.remove(value)
        }
    }

    protected fun String.invertValue(): String = if (this.contains("!")) this.replace("!", "") else "!${this}"
}
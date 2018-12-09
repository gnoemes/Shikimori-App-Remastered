package com.gnoemes.shikimori.presentation.view.search.filter.strategy

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.Toolbar
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.utils.*
import com.google.android.material.chip.Chip
import com.santalu.widget.ReSpinner
import kotlinx.android.synthetic.main.dialog_base_bottom_sheet.view.*
import kotlinx.android.synthetic.main.layout_category_with_button.view.*
import kotlinx.android.synthetic.main.layout_category_with_chip_group.view.*

abstract class BaseFilterStrategy(
        private val view: View,
        private val context: Context,
        private val clickListener: (Pair<String, MutableList<FilterItem>>) -> Unit
) : FilterStrategy {

    private var appliedFilters: HashMap<String, MutableList<FilterItem>> = HashMap()
    protected var spinner: ReSpinner? = null

    ///////////////////////////////////////////////////////////////////////////
    // INTERFACE IMPLEMENTATION
    ///////////////////////////////////////////////////////////////////////////

    override fun init(appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        this.appliedFilters = appliedFilters
        initSpinner(view.toolbar)
        initChipContainers()
        initListContainers()
    }

    override fun reset() {
        appliedFilters.clear()
        init(appliedFilters)
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONTAINERS INITIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    private fun initSpinner(toolbar: Toolbar) {
        spinner = ReSpinner(toolbar.context)
        spinner?.adapter = getSpinnerAdapter(getSpinnerArray())
        spinner?.onItemClickListener = spinnerItemClickListener

        toolbar.removeViewAt(0)
        toolbar.addView(spinner, 0)

        val value = getAppliedFilter(SearchConstants.ORDER)?.find { it.action == SearchConstants.ORDER }?.value

        val selection = SearchConstants.ORDER_BY.values().find { it.equalsType(value) }
        val selectionPos = getSortPosition(selection)

        spinner?.setSelection(selectionPos, false)
    }

    private fun initChipContainers() {
        chipContainers.forEach { initChipContainer(it.second, it.first) }
    }

    private fun initListContainers() {
        listContainers.forEach { initListContainer(it.second, it.first) }
    }

    private fun initChipContainer(container: View, key: String) {
        initContainer(container, key) { category ->
            with(container) {
                visible()
                categoryNameView.text = category.first
                chipGroup.removeAllViews()
                category.second.forEach { chipGroup.addView(convertChip(it)) }
            }
        }
    }

    private fun initListContainer(container: View, key: String) {
        initContainer(container, key) { category ->
            with(container) {
                visible()
                val names = convertSelected(getAppliedFilter(category.second.firstOrNull()?.action))

                categoryName.text = category.first
                selectedValuesView.text = names
                container.setOnClickListener { clickListener.invoke(category) }
            }
        }
    }

    private fun initContainer(container: View, key: String, init: (Pair<String, MutableList<FilterItem>>) -> Unit) {
        val category = getFilter(key)

        if (category != null) {
            init.invoke(category)
        } else {
            container.gone()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // STRATEGY METHODS
    ///////////////////////////////////////////////////////////////////////////

    abstract val filters: HashMap<String, Pair<String, MutableList<FilterItem>>>
    abstract val chipContainers: List<Pair<String, View>>
    abstract val listContainers: List<Pair<String, View>>
    abstract val spinnerItemClickListener: AdapterView.OnItemClickListener
    abstract fun getSpinnerArray(): Int
    abstract fun getSortPosition(selection: SearchConstants.ORDER_BY?): Int

    ///////////////////////////////////////////////////////////////////////////
    // FILTER DATA STRUCTURE METHODS
    ///////////////////////////////////////////////////////////////////////////

    protected fun clearAndAddToSelected(key: String, filterItem: FilterItem) {
        appliedFilters[key]?.clear()
        addToSelected(key, filterItem)
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

    protected open fun getAppliedFilter(key: String?): MutableList<FilterItem>? {
        return appliedFilters[key]
    }

    protected open fun getAppliedFilter(key: String, value: String?): FilterItem? {
        return appliedFilters[key]?.find { it.value == value }
    }

    protected open fun getFilter(key: String): Pair<String, MutableList<FilterItem>>? {
        return filters[key]
    }

    ///////////////////////////////////////////////////////////////////////////
    // UTIL METHODS
    ///////////////////////////////////////////////////////////////////////////

    private fun getSpinnerAdapter(stringArray: Int): SpinnerAdapter? {
        return ArrayAdapter(context, R.layout.item_spinner_toolbar_normal, context.resources.getStringArray(stringArray))
    }

    private fun convertSelected(items: MutableList<FilterItem>?): String {
        if (items.isNullOrEmpty()) return context.getString(R.string.filter_not_selected)

        val builder = StringBuilder()
        val divider = ", "
        items.forEach { builder.append(it.localizedText).append(divider) }
        builder.replace(builder.lastIndexOf(divider), builder.length - 1, "")
        return builder.toString()
    }

    protected open fun convertChip(item: FilterItem): Chip {
        val chip = context.layoutInflater().inflate(R.layout.view_filter_chip, null) as Chip

        fun tintActive() {
            chip.background?.tint(context.colorAttr(R.attr.colorAccent))
            chip.setTextColor(context.colorAttr(R.attr.colorOnSecondary))
        }

        fun tintDefault() {
            chip.background?.tint(context.colorAttr(R.attr.colorPrimaryDark))
            chip.setTextColor(context.colorAttr(R.attr.colorOnPrimary))
        }

        return chip.apply {
            id = item.hashCode()
            tag = item.value
            text = item.localizedText
            isChecked = getAppliedFilter(item.action, item.value)?.value == item.value
            if (isChecked) tintActive()
            setOnCheckedChangeListener { _, isChecked ->
                when {
                    isChecked -> {
                        addToSelected(item.action, item)
                        tintActive()
                    }
                    else -> {
                        removeFromSelected(item.action, item)
                        tintDefault()
                    }
                }
            }
        }
    }
}
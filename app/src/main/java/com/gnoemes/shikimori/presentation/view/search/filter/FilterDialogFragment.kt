package com.gnoemes.shikimori.presentation.view.search.filter

import android.os.Bundle
import android.view.View
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.MultiCheckDialogFragment
import com.gnoemes.shikimori.presentation.view.search.filter.provider.FilterResourceConverterImpl
import com.gnoemes.shikimori.presentation.view.search.filter.provider.FilterResourceProvider
import com.gnoemes.shikimori.presentation.view.search.filter.provider.FilterResourceProviderImpl
import com.gnoemes.shikimori.presentation.view.search.filter.strategy.FilterAnimeStrategy
import com.gnoemes.shikimori.presentation.view.search.filter.strategy.FilterMangaStrategy
import com.gnoemes.shikimori.presentation.view.search.filter.strategy.FilterRanobeStrategy
import com.gnoemes.shikimori.presentation.view.search.filter.strategy.FilterStrategy
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.withArgs
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class FilterDialogFragment : BaseBottomSheetDialogFragment(), MultiCheckDialogFragment.DialogCallback {

    companion object {
        private const val FILTERS_KEY = "FILTERS_KEY"
        private const val TYPE_KEY = "TYPE_KEY"
        fun newInstance(type: Type, filters: HashMap<String, MutableList<FilterItem>>?) = FilterDialogFragment()
                .withArgs {
                    putSerializable(TYPE_KEY, type)
                    putSerializable(FILTERS_KEY, filters)
                }
    }

    private lateinit var type: Type
    private lateinit var strategy: FilterStrategy
    private var appliedFilters = HashMap<String, MutableList<FilterItem>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getSerializable(TYPE_KEY) as? Type ?: Type.ANIME
        appliedFilters = getAppliedFilters(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resourceProvider: FilterResourceProvider = FilterResourceProviderImpl(view.context, FilterResourceConverterImpl())

        strategy = when (type) {
            Type.ANIME -> FilterAnimeStrategy(view, resourceProvider, { showSelectDialog(it) })
            Type.MANGA -> FilterMangaStrategy(view, resourceProvider, { showSelectDialog(it) })
            else -> FilterRanobeStrategy(view, resourceProvider, { showSelectDialog(it) })
        }

        strategy.init(appliedFilters)

        with(toolbar) {
            title = null
            addBackButton(R.drawable.ic_close) { dismiss() }
            inflateMenu(R.menu.menu_filter)
            setOnMenuItemClickListener { onAcceptFilters(); true }
        }

        resetBtn.onClick { strategy.reset() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(FILTERS_KEY, appliedFilters)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.fragment_filter

    ///////////////////////////////////////////////////////////////////////////
    // CALLBACKS
    ///////////////////////////////////////////////////////////////////////////

    private fun onAcceptFilters() {
        (targetFragment as? FilterCallback)?.onFiltersSelected(appliedFilters)
        dismiss()
    }

    override fun dialogItemCallback(tag: String?, items: List<String>) {
        val newAppliedFilters = convertFilters(items, tag!!)
        appliedFilters[newAppliedFilters.first] = newAppliedFilters.second
        strategy.init(appliedFilters)
    }

    private fun showSelectDialog(category: Pair<String, MutableList<FilterItem>>) {
        val dialog = MultiCheckDialogFragment.newInstance()
        dialog.apply {
            setTitle(category.first)
            setItems(convertFilters(category, appliedFilters))
        }.show(childFragmentManager, category.second.firstOrNull()?.action)
    }

    ///////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////

    @Suppress("UNCHECKED_CAST")
    private fun getAppliedFilters(savedInstanceState: Bundle?): HashMap<String, MutableList<FilterItem>> {
        val serializable =
                if (savedInstanceState != null) savedInstanceState.getSerializable(FILTERS_KEY)
                else arguments?.getSerializable(FILTERS_KEY)

        return serializable as? HashMap<String, MutableList<FilterItem>> ?: HashMap()
    }

    private fun convertFilters(category: Pair<String, MutableList<FilterItem>>, applied: HashMap<String, MutableList<FilterItem>>): List<Pair<Boolean, Pair<String, String>>> {
        return category.second.map { filter ->
            val isApplied = applied.containsKey(filter.action) && applied[filter.action]?.find { it.value == filter.value } != null
            val action = Gson().toJson(filter)
            return@map Pair(isApplied, Pair(filter.localizedText!!, action))
        }
    }

    private fun convertFilters(applied: List<String>, tag: String): Pair<String, MutableList<FilterItem>> {
        val items = applied.map { Gson().fromJson(it, FilterItem::class.java) }.toMutableList()
        return Pair(tag, items)
    }
}
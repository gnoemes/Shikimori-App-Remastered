package com.gnoemes.shikimori.presentation.view.search.filter.strategy

import android.content.Context
import android.view.View
import android.widget.AdapterView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import com.gnoemes.shikimori.presentation.view.search.filter.provider.FilterResourceProvider

class FilterMangaStrategy(
        private val view: View,
        private val resourceProvider: FilterResourceProvider,
        clickListener: (FilterCategory) -> Unit,
        context: Context = view.context
) : BaseFilterStrategy(view, context, clickListener) {

    private val mangaFilters by lazy { resourceProvider.getMangaFilters() }
    private val mangaChipContainers by lazy {
        listOf(
                Container(SearchConstants.STATUS, view),
                Container(SearchConstants.RATE, view)
        )
    }

    private val mangaListContainers by lazy {
        listOf(
                Container(SearchConstants.GENRE, view),
                Container(SearchConstants.TYPE, view),
                Container(SearchConstants.SEASON, view)
        )
    }

    override val filters: HashMap<String, FilterCategory>
        get() = mangaFilters

    override val chipContainers: List<Container>
        get() = mangaChipContainers

    override val listContainers: List<Container>
        get() = mangaListContainers

    override val spinnerItemClickListener: AdapterView.OnItemClickListener
        get() = AdapterView.OnItemClickListener { _, _, position, _ ->
            val filterItem = when (position) {
                0 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.NAME.toString(), null)
                1 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.AIRED_ON.toString(), null)
                2 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.ID.toString(), null)
                3 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.STATUS.toString(), null)
                4 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.RANDOM.toString(), null)
                5 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.VOLUMES.toString(), null)
                6 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.CHAPTERS.toString(), null)
                7 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.RANKED.toString(), null)
                8 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.POPULARITY.toString(), null)
                else -> null
            }

            filterItem?.let { clearAndAddToSelected(SearchConstants.ORDER, it) }
        }

    override fun getSpinnerArray(): Int = R.array.manga_search_sort

    override fun getSortPosition(selection: SearchConstants.ORDER_BY?): Int {
        return when (selection) {
            SearchConstants.ORDER_BY.NAME -> 0
            SearchConstants.ORDER_BY.AIRED_ON -> 1
            SearchConstants.ORDER_BY.ID -> 2
            SearchConstants.ORDER_BY.STATUS -> 3
            SearchConstants.ORDER_BY.RANDOM -> 4
            SearchConstants.ORDER_BY.VOLUMES -> 5
            SearchConstants.ORDER_BY.CHAPTERS -> 6
            SearchConstants.ORDER_BY.RANKED -> 7
            else -> 8
        }
    }
}
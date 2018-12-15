package com.gnoemes.shikimori.presentation.view.search.filter.strategy

import android.content.Context
import android.view.View
import android.widget.AdapterView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.SearchConstants
import com.gnoemes.shikimori.entity.search.presentation.FilterCategory
import com.gnoemes.shikimori.presentation.view.search.filter.provider.FilterResourceProvider
import kotlinx.android.synthetic.main.fragment_filter.view.*

class FilterAnimeStrategy(
        private val view: View,
        private val resourceProvider: FilterResourceProvider,
        clickListener: (FilterCategory) -> Unit,
        context: Context = view.context
) : BaseFilterStrategy(view, context, clickListener) {

    private val animeFilters by lazy { resourceProvider.getAnimeFilters() }

    private val animeChipContainers by lazy {
        listOf(
                Container(SearchConstants.STATUS, view.statusGroup),
                Container(SearchConstants.DURATION, view.durationGroup),
                Container(SearchConstants.RATE, view.rateGroup),
                Container(SearchConstants.AGE_RATING, view.ageGroup)
        )
    }

    private val animeListContainers by lazy {
        listOf(
                Container(SearchConstants.GENRE, view.genresGroup),
                Container(SearchConstants.TYPE, view.typeGroup)
        )
    }

    override val filters: HashMap<String, FilterCategory>
        get() = animeFilters

    override val chipContainers: List<Container>
        get() = animeChipContainers

    override val listContainers: List<Container>
        get() = animeListContainers

    override val spinnerItemClickListener: AdapterView.OnItemClickListener
        get() = AdapterView.OnItemClickListener { _, _, position, _ ->
            val filterItem = when (position) {
                0 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.NAME.toString(), null)
                1 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.AIRED_ON.toString(), null)
                2 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.TYPE.toString(), null)
                3 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.ID.toString(), null)
                4 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.STATUS.toString(), null)
                5 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.RANDOM.toString(), null)
                6 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.EPISODES.toString(), null)
                7 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.RANKED.toString(), null)
                8 -> FilterItem(SearchConstants.ORDER, SearchConstants.ORDER_BY.POPULARITY.toString(), null)
                else -> null
            }

            filterItem?.let { clearAndAddToSelected(SearchConstants.ORDER, it) }
        }

    override fun getSpinnerArray(): Int = R.array.anime_search_sort

    override fun getSortPosition(selection: SearchConstants.ORDER_BY?): Int {
        return when (selection) {
            SearchConstants.ORDER_BY.NAME -> 0
            SearchConstants.ORDER_BY.AIRED_ON -> 1
            SearchConstants.ORDER_BY.TYPE -> 2
            SearchConstants.ORDER_BY.ID -> 3
            SearchConstants.ORDER_BY.STATUS -> 4
            SearchConstants.ORDER_BY.RANDOM -> 5
            SearchConstants.ORDER_BY.EPISODES -> 6
            SearchConstants.ORDER_BY.RANKED -> 7
            else -> 8
        }
    }
}
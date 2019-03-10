package com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterMainGenreCategory
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.presentation.view.search.filter.adapter.FilterChipAdapter
import com.gnoemes.shikimori.utils.inflate
import com.google.android.flexbox.FlexboxLayoutManager
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_filter_genre_section.view.*

class FilterGenreMainCategoryAdapterDelegate(
        private val invertCallback: (FilterType, FilterViewModel) -> Unit,
        private val selectCallback: (FilterType, FilterViewModel) -> Unit
) : AbsListItemAdapterDelegate<FilterMainGenreCategory, Any, FilterGenreMainCategoryAdapterDelegate.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterMainGenreCategory

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_filter_genre_section))

    override fun onBindViewHolder(item: FilterMainGenreCategory, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.recyclerView.setRecycledViewPool(sharedPool)
        }

        fun bind(item: FilterMainGenreCategory) {
            with(itemView) {
                categoryNameView.setText(R.string.filter_genres_main)
                val chipAdapter = FilterChipAdapter(FilterType.GENRE, invertCallback, selectCallback).apply { if (!hasObservers()) setHasStableIds(true) }

                with(recyclerView) {
                    adapter = chipAdapter
                    layoutManager = FlexboxLayoutManager(context)
                    itemAnimator = null
                }

                chipAdapter.bind(item.filters)
            }
        }
    }
}
package com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterOtherGenreCategory
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_filter_genre_section.view.*

class FilterGenreOtherCategoryAdapterDelegate(
        private val invertCallback: (FilterType, FilterViewModel) -> Unit,
        private val selectCallback: (FilterType, FilterViewModel) -> Unit
) : AbsListItemAdapterDelegate<FilterOtherGenreCategory, Any, FilterGenreOtherCategoryAdapterDelegate.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterOtherGenreCategory

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_filter_genre_section))

    override fun onBindViewHolder(item: FilterOtherGenreCategory, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.recyclerView.apply {
                setRecycledViewPool(sharedPool)
                itemAnimator = null
            }
        }

        fun bind(item: FilterOtherGenreCategory) {
            with(itemView) {
                categoryNameView.setText(R.string.filter_genres_other)

                val headerAdapter = FilterGenreWithHeaderAdapter(invertCallback, selectCallback)

                with(recyclerView) {
                    adapter = headerAdapter
                    layoutManager = LinearLayoutManager(context)
                }

                headerAdapter.bindItems(item.filters)
            }
        }
    }
}
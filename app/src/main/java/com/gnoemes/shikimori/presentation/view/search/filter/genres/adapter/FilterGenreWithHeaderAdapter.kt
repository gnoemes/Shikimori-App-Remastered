package com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterGenreItem
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.presentation.view.search.filter.adapter.FilterChipAdapter
import com.gnoemes.shikimori.utils.clearAndAddAll
import com.gnoemes.shikimori.utils.inflate
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.item_genres_with_header.view.*

class FilterGenreWithHeaderAdapter(
        private val invertCallback: (FilterType, FilterViewModel) -> Unit,
        private val selectCallback: (FilterType, FilterViewModel) -> Unit
) : RecyclerView.Adapter<FilterGenreWithHeaderAdapter.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    private val items = mutableListOf<FilterGenreItem>()

    fun bindItems(newItems: List<FilterGenreItem>) {
        items.clearAndAddAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_genres_with_header))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.recyclerView.setRecycledViewPool(sharedPool)
        }

        fun bind(item: FilterGenreItem) {
            with(itemView) {
                headerView.text = item.header
                val chipAdapter = FilterChipAdapter(FilterType.GENRE, invertCallback, selectCallback).apply { if (!hasObservers()) setHasStableIds(true) }

                with(recyclerView) {
                    adapter = chipAdapter
                    layoutManager = FlexboxLayoutManager(context)
                }

                chipAdapter.bind(item.filters)
            }
        }
    }
}
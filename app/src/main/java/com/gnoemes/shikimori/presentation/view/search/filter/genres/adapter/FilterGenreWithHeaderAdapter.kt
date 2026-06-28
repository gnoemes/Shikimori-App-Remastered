package com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemGenresWithHeaderBinding
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterGenreItem
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.presentation.view.search.filter.adapter.FilterChipAdapter
import com.gnoemes.shikimori.utils.clearAndAddAll
import com.gnoemes.shikimori.utils.inflate
import com.google.android.flexbox.FlexboxLayoutManager

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
            ViewHolder(ItemGenresWithHeaderBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemGenresWithHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerView.setRecycledViewPool(sharedPool)
        }

        fun bind(item: FilterGenreItem) {
            binding.headerView.text = item.header
            val chipAdapter = FilterChipAdapter(FilterType.GENRE, invertCallback, selectCallback).apply { if (!hasObservers()) setHasStableIds(true) }

            with(binding.recyclerView) {
                adapter = chipAdapter
                layoutManager = FlexboxLayoutManager(context)
            }

            chipAdapter.bind(item.filters)
        }
    }
}
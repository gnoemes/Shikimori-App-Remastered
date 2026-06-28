package com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemFilterGenreSectionBinding
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterOtherGenreCategory
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FilterGenreOtherCategoryAdapterDelegate(
        private val invertCallback: (FilterType, FilterViewModel) -> Unit,
        private val selectCallback: (FilterType, FilterViewModel) -> Unit
) : AbsListItemAdapterDelegate<FilterOtherGenreCategory, Any, FilterGenreOtherCategoryAdapterDelegate.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterOtherGenreCategory

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemFilterGenreSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: FilterOtherGenreCategory, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemFilterGenreSectionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerView.apply {
                setRecycledViewPool(sharedPool)
                itemAnimator = null
            }
        }

        fun bind(item: FilterOtherGenreCategory) {
            binding.categoryNameView.setText(R.string.filter_genres_other)

            val headerAdapter = FilterGenreWithHeaderAdapter(invertCallback, selectCallback)

            with(binding.recyclerView) {
                adapter = headerAdapter
                layoutManager = LinearLayoutManager(context)
            }

            headerAdapter.bindItems(item.filters)
        }
    }
}
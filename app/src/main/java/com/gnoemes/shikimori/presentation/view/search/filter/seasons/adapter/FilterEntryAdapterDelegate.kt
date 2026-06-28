package com.gnoemes.shikimori.presentation.view.search.filter.seasons.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemChipEntryBinding
import com.gnoemes.shikimori.entity.search.presentation.FilterEntryViewModel
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FilterEntryAdapterDelegate(
        private val closeCallback: (FilterEntryViewModel) -> Unit
) : AbsListItemAdapterDelegate<FilterEntryViewModel, Any, FilterEntryAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterEntryViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemChipEntryBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: FilterEntryViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemChipEntryBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: FilterEntryViewModel

        init {
            binding.chip.setOnCloseIconClickListener { closeCallback.invoke(item) }
        }

        fun bind(item: FilterEntryViewModel) {
            this.item = item
            binding.chip.text = item.value
        }
    }
}
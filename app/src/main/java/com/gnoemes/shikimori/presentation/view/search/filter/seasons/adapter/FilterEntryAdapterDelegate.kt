package com.gnoemes.shikimori.presentation.view.search.filter.seasons.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.search.presentation.FilterEntryViewModel
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_chip_entry.view.*

class FilterEntryAdapterDelegate(
        private val closeCallback: (FilterEntryViewModel) -> Unit
) : AbsListItemAdapterDelegate<FilterEntryViewModel, Any, FilterEntryAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterEntryViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_chip_entry))

    override fun onBindViewHolder(item: FilterEntryViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: FilterEntryViewModel

        init {
            itemView.chip.setOnCloseIconClickListener { closeCallback.invoke(item) }
        }

        fun bind(item: FilterEntryViewModel) {
            this.item = item
            itemView.chip.text = item.value
        }
    }
}
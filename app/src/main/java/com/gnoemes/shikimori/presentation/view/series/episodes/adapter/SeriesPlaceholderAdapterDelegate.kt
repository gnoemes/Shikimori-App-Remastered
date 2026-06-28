package com.gnoemes.shikimori.presentation.view.series.episodes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemSeriesEmptyPlaceholderBinding
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class SeriesPlaceholderAdapterDelegate : AbsListItemAdapterDelegate<SeriesPlaceholderItem, Any, SeriesPlaceholderAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is SeriesPlaceholderItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemSeriesEmptyPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: SeriesPlaceholderItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ItemSeriesEmptyPlaceholderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SeriesPlaceholderItem) {
            binding.titleView.setText(item.title)
            binding.descriptionView.setText(item.description)
        }

    }
}
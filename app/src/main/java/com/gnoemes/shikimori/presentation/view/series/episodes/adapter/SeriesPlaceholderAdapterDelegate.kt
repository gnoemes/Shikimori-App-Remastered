package com.gnoemes.shikimori.presentation.view.series.episodes.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_series_empty_placeholder.view.*

class SeriesPlaceholderAdapterDelegate : AbsListItemAdapterDelegate<SeriesPlaceholderItem, Any, SeriesPlaceholderAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is SeriesPlaceholderItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_series_empty_placeholder))

    override fun onBindViewHolder(item: SeriesPlaceholderItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: SeriesPlaceholderItem) {
            with(itemView) {
                titleView.setText(item.title)
                descriptionView.setText(item.description)
            }
        }

    }
}
package com.gnoemes.shikimori.presentation.view.calendar.adapter

import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.entity.series.presentation.SeriesPlaceholderItem
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.presentation.view.series.episodes.adapter.SeriesPlaceholderAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader

class CalendarAdapter(
        imageLoader: ImageLoader,
        callback: (id: Long) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(0, CalendarCategoryAdapterDelegate(imageLoader, callback))
            addDelegate(SeriesPlaceholderAdapterDelegate())
        }
    }

    override fun getItemId(position: Int): Long {
        return if (items[position] is CalendarViewModel) (items[position] as CalendarViewModel).date.hashCode().toLong()
        else items[position].hashCode().toLong()
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is CalendarViewModel && newItem is CalendarViewModel -> oldItem.date == newItem.date
        else -> oldItem is SeriesPlaceholderItem && newItem is SeriesPlaceholderItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is CalendarViewModel && newItem is CalendarViewModel -> oldItem == newItem
        else -> oldItem is SeriesPlaceholderItem && newItem is SeriesPlaceholderItem
    }
}
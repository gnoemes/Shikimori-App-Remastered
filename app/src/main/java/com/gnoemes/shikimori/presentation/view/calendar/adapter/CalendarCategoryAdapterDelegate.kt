package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemCalendarBinding
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.StartSnapHelper
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class CalendarCategoryAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (id: Long) -> Unit
) : AbsListItemAdapterDelegate<CalendarViewModel, Any, CalendarCategoryAdapterDelegate.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean = item is CalendarViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: CalendarViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val vh = holder as ViewHolder
        vh.binding.recyclerView.adapter = null
        vh.binding.recyclerView.layoutManager = null
        vh.binding.dateTextView.text = null
    }

    inner class ViewHolder(val binding: ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root) {

        private val snapOffset by lazy { binding.root.context.dp(16) }
        private lateinit var item: CalendarViewModel

        init {
            if (binding.recyclerView.onFlingListener == null) {
                val snapHelper = StartSnapHelper(snapOffset)
                snapHelper.attachToRecyclerView(binding.recyclerView)
            }

            binding.recyclerView.apply {
                setHasFixedSize(true)
                setRecycledViewPool(sharedPool)
                setItemViewCacheSize(20)
                addItemDecoration(HorizontalSpaceItemDecorator(context.dp(8), snapOffset))
            }
        }

        fun bind(item: CalendarViewModel) {
            this.item = item
            binding.dateTextView.text = item.date
            with(binding.recyclerView) {
                isNestedScrollingEnabled = false
                adapter = CalendarAnimeAdapter(binding.root.context, imageLoader, callback, item.items).apply { if (!hasObservers()) setHasStableIds(true) }
                layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
            }
        }

    }
}
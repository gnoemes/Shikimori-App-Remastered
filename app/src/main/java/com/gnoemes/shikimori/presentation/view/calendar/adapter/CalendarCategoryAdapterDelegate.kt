package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.StartSnapHelper
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_calendar.view.*

class CalendarCategoryAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (id: Long) -> Unit
) : AbsListItemAdapterDelegate<CalendarViewModel, Any, CalendarCategoryAdapterDelegate.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean = item is CalendarViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_calendar))

    override fun onBindViewHolder(item: CalendarViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as ViewHolder).itemView.apply {
            recyclerView.adapter = null
            recyclerView.layoutManager = null
            dateTextView.text = null
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val snapOffset by lazy { itemView.context.dp(16) }
        private lateinit var item: CalendarViewModel

        init {
            if (itemView.recyclerView.onFlingListener == null) {
                val snapHelper = StartSnapHelper(snapOffset)
                snapHelper.attachToRecyclerView(itemView.recyclerView)
            }

            itemView.recyclerView.apply {
                setHasFixedSize(true)
                setRecycledViewPool(sharedPool)
                setItemViewCacheSize(20)
                addItemDecoration(HorizontalSpaceItemDecorator(context.dp(8), snapOffset))
            }
        }

        fun bind(item: CalendarViewModel) {
            this.item = item
            with(itemView) {
                dateTextView.text = item.date
                with(recyclerView) {
                    isNestedScrollingEnabled = false
                    adapter = CalendarAnimeAdapter(itemView.context, imageLoader, callback, item.items).apply { if (!hasObservers()) setHasStableIds(true) }
                    layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
                }
            }
        }

    }
}
package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.utils.images.ImageLoader
import kotlinx.android.synthetic.main.item_calendar.view.*

class CalendarAdapter(
        private val imageLoader: ImageLoader,
        private val callback: (id: Long) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    private val items = mutableListOf<CalendarViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_calendar, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun bindItems(viewModels: List<CalendarViewModel>) {
        val callback = DiffCallback(items, viewModels)
        items.clear()
        items.addAll(viewModels)
        DiffUtil.calculateDiff(callback)
                .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
            private val oldItems: List<Any>,
            private val newItems: List<Any>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bind(item: CalendarViewModel) {
            with(itemView) {
                dateTextView.text = item.date

                with(recyclerView) {
                    adapter = CalendarAnimeAdapter(imageLoader, callback, item.items)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
                    setRecycledViewPool(sharedPool)
                }
            }
        }

    }
}
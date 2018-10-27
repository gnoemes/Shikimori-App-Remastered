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
        val oldData = items.toMutableList()
        items.clear()
        items.addAll(viewModels)

        DiffUtil.calculateDiff(DiffCallback(oldData, items))
                .dispatchUpdatesTo(this)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.apply {
            recyclerView.adapter = null
            dateTextView.text = null
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bind(item: CalendarViewModel) {
            with(itemView) {
                dateTextView.text = item.date

                with(recyclerView) {
                    adapter = CalendarAnimeAdapter(imageLoader, callback, item.items)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
                    setHasFixedSize(true)
                    setRecycledViewPool(sharedPool)
                    setItemViewCacheSize(20)
                }
            }
        }
    }

    private inner class DiffCallback(
            private val oldItems: MutableList<CalendarViewModel>,
            private val newItems: MutableList<CalendarViewModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem == newItem
        }
    }

}
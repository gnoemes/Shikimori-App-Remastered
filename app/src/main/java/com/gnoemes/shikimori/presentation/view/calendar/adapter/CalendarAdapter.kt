package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        items.clear()
        items.addAll(viewModels)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val animeAdapter: CalendarAnimeAdapter = CalendarAnimeAdapter(imageLoader, callback)

        fun bind(item: CalendarViewModel) {
            with(itemView) {
                dateTextView.text = item.date

                with(recyclerView) {
                    adapter = animeAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
                    setRecycledViewPool(sharedPool)
                }

                animeAdapter.bindItems(item.items)
            }
        }

    }


}
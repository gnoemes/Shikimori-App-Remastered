package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarAnimeItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.images.Prefetcher
import com.gnoemes.shikimori.utils.images.SimplePrefetcher
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.item_calendar_anime.view.*

class CalendarAnimeAdapter(
        context: Context,
        private val imageLoader: ImageLoader,
        private val callback: (id: Long) -> Unit,
        private val items: List<CalendarAnimeItem>
) : RecyclerView.Adapter<CalendarAnimeAdapter.ViewHolder>() {

    init {
        val prefetcher: Prefetcher = SimplePrefetcher(context)
        prefetcher.prefetch(items.map { it.image.original })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_calendar_anime))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.apply {
            imageLoader.clearImage(imageView)
            cardView.setOnClickListener(null)
            nameView.text = null
            typeView.text = null
            episodeView.text = null
            nextEpisodeDateView.text = null
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item : CalendarAnimeItem

        init {
            itemView.cardView.onClick { callback.invoke(item.id) }
        }

        fun bind(item: CalendarAnimeItem) {
            this.item = item
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.image.original)
                nameView.text = item.name
                typeView.text = item.type.type
                episodeView.text = item.episodeText
                nextEpisodeDateView.text = item.nextEpisode
                nextEpisodeDateView.visibleIf { item.isToday }
            }
        }
    }
}

package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarAnimeItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.unknownIfZero
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.item_calendar_anime.view.*

class CalendarAnimeAdapter(
        private val imageLoader: ImageLoader,
        private val callback: (id: Long) -> Unit
) : RecyclerView.Adapter<CalendarAnimeAdapter.ViewHolder>() {

    val items = mutableListOf<CalendarAnimeItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_calendar_anime, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun bindItems(animeViewModels: List<CalendarAnimeItem>) {
        items.clear()
        items.addAll(animeViewModels)

        DiffUtil.calculateDiff(DiffCallback(items, animeViewModels))
                .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
            private val newItems: List<CalendarAnimeItem>,
            private val oldItems: List<CalendarAnimeItem>
    ) : DiffUtil.Callback() {
        override fun getNewListSize(): Int = newItems.size
        override fun getOldListSize(): Int = oldItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: CalendarAnimeItem) {
            with(itemView) {
                imageLoader.clearImage(imageView)
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)

                val episodes = String.format(context.getString(R.string.episodes_format), item.episodesAired.unknownIfZero(), item.episodes.unknownIfZero())
                val nameText = item.name + episodes

                nameView.text = nameText
                typeView.text = item.type.type

                val episodeText = String.format(context.getString(R.string.episode_number), item.episodeNext)
                episodeView.text = episodeText

                val nextEpisodeText =
                        if (item.durationToAired.isNullOrEmpty()) context.getText(R.string.calendar_must_aired)
                        else String.format(context.getString(R.string.episode_in), item.durationToAired)
                nextEpisodeDateView.text = nextEpisodeText

                nextEpisodeDateView.visibleIf { item.isToday }
                cardView.onClick { Glide.get(this.context).clearMemory() }
            }
        }

    }
}

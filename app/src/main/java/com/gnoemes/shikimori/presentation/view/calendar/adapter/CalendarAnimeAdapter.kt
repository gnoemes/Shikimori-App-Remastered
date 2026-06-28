package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemCalendarAnimeBinding
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarAnimeItem
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.images.Prefetcher
import com.gnoemes.shikimori.utils.images.SimplePrefetcher
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf

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
            ViewHolder(ItemCalendarAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        imageLoader.clearImage(holder.binding.imageView)
    }

    inner class ViewHolder(val binding: ItemCalendarAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: CalendarAnimeItem

        init {
            binding.materialCardView.onClick { callback.invoke(item.id) }
        }

        fun bind(item: CalendarAnimeItem) {
            this.item = item
            imageLoader.setImageListItem(binding.imageView, item.image.original)
            binding.nameView.text = item.name
            binding.descriptionView.text = item.description

            if (item.status != null) {
                binding.rateView.setIconResource(getRateIcon(item.status))
                binding.rateView.setIconTintResource(getRateColor(item.status))
            }

            binding.rateView.visibleIf { item.status != null }
            binding.seasonLastView.visibleIf { item.isLast }
        }

        @DrawableRes
        private fun getRateIcon(status: RateStatus): Int = when (status) {
            RateStatus.WATCHING -> R.drawable.ic_play_rate
            RateStatus.PLANNED -> R.drawable.ic_planned
            RateStatus.REWATCHING -> R.drawable.ic_replay
            RateStatus.COMPLETED -> R.drawable.ic_check
            RateStatus.ON_HOLD -> R.drawable.ic_pause_rate
            RateStatus.DROPPED -> R.drawable.ic_close
        }

        @ColorRes
        private fun getRateColor(status: RateStatus) = when (status) {
            RateStatus.WATCHING -> R.color.rate_default_dark
            RateStatus.PLANNED -> R.color.rate_default_dark
            RateStatus.REWATCHING -> R.color.rate_default_dark
            RateStatus.COMPLETED -> R.color.rate_watched_dark
            RateStatus.ON_HOLD -> R.color.rate_on_hold_dark
            RateStatus.DROPPED -> R.color.rate_dropped_dark
        }
    }
}

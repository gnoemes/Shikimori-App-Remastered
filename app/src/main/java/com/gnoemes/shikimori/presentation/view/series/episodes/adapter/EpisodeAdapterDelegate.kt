package com.gnoemes.shikimori.presentation.view.series.episodes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemEpisodeBinding
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate


class EpisodeAdapterDelegate(
        private val callback: (EpisodeViewModel) -> Unit,
        private val episodeChanged: (EpisodeViewModel, Boolean) -> Unit,
        private val longPressListener: (EpisodeViewModel) -> Unit
) : AbsListItemAdapterDelegate<EpisodeViewModel, Any, EpisodeAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is EpisodeViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: EpisodeViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: EpisodeViewModel

        init {
            binding.episodeContainer.onClick { callback.invoke(item) }
            binding.episodeContainer.setOnLongClickListener { longPressListener.invoke(item); false }
            binding.watchedView.onClick { episodeChanged.invoke(item, !item.isWatched) }
        }

        fun bind(item: EpisodeViewModel) {
            this.item = item
            val context = binding.root.context
            val episodeName = String.format(context.getString(R.string.episode_number), item.index)
            binding.episodeNameView.text = episodeName
            binding.watchedView.isSelected = item.isWatched
            binding.progressBar.visibleIf { item.state == EpisodeViewModel.State.Loading }
            binding.watchedView.visibleIf { !item.isGuest && item.state != EpisodeViewModel.State.Loading }
            binding.currentEpisodeView.visibleIf { item.isOpened }
        }

    }
}
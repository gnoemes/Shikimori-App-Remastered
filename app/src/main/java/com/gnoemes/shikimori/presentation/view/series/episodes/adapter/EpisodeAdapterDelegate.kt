package com.gnoemes.shikimori.presentation.view.series.episodes.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.utils.*
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_episode.view.*


class EpisodeAdapterDelegate(
        private val callback: (EpisodeViewModel) -> Unit,
        private val episodeChanged: (EpisodeViewModel, Boolean) -> Unit,
        private val longPressListener: (EpisodeViewModel) -> Unit
) : AbsListItemAdapterDelegate<EpisodeViewModel, Any, EpisodeAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is EpisodeViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_episode))

    override fun onBindViewHolder(item: EpisodeViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: EpisodeViewModel

        init {
            itemView.episodeContainer.onClick { callback.invoke(item) }
            itemView.episodeContainer.setOnLongClickListener { longPressListener.invoke(item);false }
            itemView.watchedView.onClick { episodeChanged.invoke(item, !item.isWatched) }
        }

        fun bind(item: EpisodeViewModel) {
            this.item = item
            with(itemView) {
                val episodeName = String.format(context.getString(R.string.episode_number), item.index)
                episodeNameView.text = episodeName
                watchedView.isSelected = item.isWatched
                progressBar.visibleIf { item.state == EpisodeViewModel.State.Loading }
                watchedView.visibleIf { item.state != EpisodeViewModel.State.Loading }
                val tintColor =
                        if (item.isWatched) R.attr.colorAccentTransparent
                        else R.attr.colorPrimary
                watchedView.background = watchedView.background.apply { tint(context.colorAttr(tintColor)) }
            }
        }

    }
}
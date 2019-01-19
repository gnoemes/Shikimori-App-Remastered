package com.gnoemes.shikimori.presentation.view.series.episodes.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.presentation.EpisodeViewModel
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_episode.view.*


class EpisodeAdapterDelegate(
        private val callback: (EpisodeViewModel) -> Unit,
        private val episodeChanged: (EpisodeViewModel, Boolean) -> Unit
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
            itemView.episodeContainer.setOnClickListener { callback.invoke(item) }
            itemView.watchedView.onClick { episodeChanged.invoke(item, !item.isWatched) }
            //block checkbox state
            itemView.watchedView.setOnCheckedChangeListener { buttonView, _ -> buttonView.isChecked = item.isWatched }
        }

        fun bind(item: EpisodeViewModel) {
            this.item = item
            with(itemView) {
                val episodeName = String.format(context.getString(R.string.episode_number), item.id)
                episodeNameView.text = episodeName
                watchedView.isChecked = item.isWatched
                progressBar.visibleIf { item.state == EpisodeViewModel.State.Loading }
                watchedView.visibleIf { item.state != EpisodeViewModel.State.Loading }
            }
        }

    }
}
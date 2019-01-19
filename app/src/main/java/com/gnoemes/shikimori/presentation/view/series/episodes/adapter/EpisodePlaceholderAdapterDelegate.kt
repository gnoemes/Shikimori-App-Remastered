package com.gnoemes.shikimori.presentation.view.series.episodes.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.presentation.EpisodePlaceholderItem
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class EpisodePlaceholderAdapterDelegate : AbsListItemAdapterDelegate<EpisodePlaceholderItem, Any, EpisodePlaceholderAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is EpisodePlaceholderItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_episode_placeholder))

    override fun onBindViewHolder(item: EpisodePlaceholderItem, holder: ViewHolder, payloads: MutableList<Any>) {}

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
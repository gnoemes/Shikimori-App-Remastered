package com.gnoemes.shikimori.presentation.view.topic.details.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.PlaceholderItem
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class CommentPlaceholderAdapterDelegate : AbsListItemAdapterDelegate<PlaceholderItem, Any, CommentPlaceholderAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is PlaceholderItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_comment_placeholder))

    override fun onBindViewHolder(item: PlaceholderItem, holder: ViewHolder, payloads: MutableList<Any>) {
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
}
package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsTagItem
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class GenreTagAdapterDelegate(
        private val callback: (DetailsAction) -> Unit
) : AbsListItemAdapterDelegate<DetailsTagItem, Any, GenreTagAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is DetailsTagItem && item.type == DetailsTagItem.TagType.GENRE

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_tag_genre))

    override fun onBindViewHolder(item: DetailsTagItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: DetailsTagItem

        init {
            itemView.onClick { callback.invoke(DetailsAction.GenreClicked(item.raw as Genre)) }
        }

        fun bind(item: DetailsTagItem) {
            this.item = item
            (itemView as Chip).text = item.name
        }

    }
}
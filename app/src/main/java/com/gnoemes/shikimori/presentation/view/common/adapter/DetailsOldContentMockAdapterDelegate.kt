package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.presentation.view.common.adapter.content.ContentAdapter
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsContentViewHolder
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class DetailsOldContentMockAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit,
        private val detailsCallback: ((DetailsAction) -> Unit)?
) : AbsListItemAdapterDelegate<Pair<DetailsContentType, DetailsContentItem>, Any, DetailsOldContentMockAdapterDelegate.ViewHolder>() {
    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is Pair<*, *>

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.layout_details_content))

    override fun onBindViewHolder(item: Pair<DetailsContentType, DetailsContentItem>, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val holder: DetailsContentViewHolder = DetailsContentViewHolder(itemView, ContentAdapter(imageLoader, navigationCallback, detailsCallback))

        fun bind(item: Pair<DetailsContentType, DetailsContentItem>) {
            holder.bind(item.first, item.second)
        }

    }
}
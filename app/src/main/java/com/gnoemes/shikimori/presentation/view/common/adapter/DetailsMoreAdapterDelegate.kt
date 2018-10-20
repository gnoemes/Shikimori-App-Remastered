package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsMoreItem
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_details_more.view.*

class DetailsMoreAdapterDelegate(
        private val detailsCallback: (DetailsAction) -> Unit
) : AbsListItemAdapterDelegate<DetailsMoreItem, Any, DetailsMoreAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is DetailsMoreItem

    override fun onBindViewHolder(item: DetailsMoreItem, holder: ViewHolder, payloads: MutableList<Any>) {
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_details_more))


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            with(itemView) {
                chronologyView.onClick { detailsCallback.invoke(DetailsAction.Chronology) }
                linksView.onClick { detailsCallback.invoke(DetailsAction.Links) }
                discussionView.onClick { detailsCallback.invoke(DetailsAction.Discussion) }
            }
        }

    }
}
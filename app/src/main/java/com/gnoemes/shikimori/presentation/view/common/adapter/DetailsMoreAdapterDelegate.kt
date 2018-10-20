package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsMoreItem
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_details_more.view.*

class DetailsMoreAdapterDelegate(
        private val detailsCallback: (DetailsAction) -> Unit
) : AdapterDelegate<MutableList<Any>>() {
    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
            items[position] is DetailsMoreItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_details_more))

    override fun onBindViewHolder(items: MutableList<Any>, pos: Int, holder: RecyclerView.ViewHolder, p3: MutableList<Any>) {
    }

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
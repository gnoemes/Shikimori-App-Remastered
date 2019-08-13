package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.InfoClickableItem
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_details_info_clickable.view.*

class InfoClickableAdapterDelegate(
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<InfoClickableItem, Any, InfoClickableAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is InfoClickableItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_details_info_clickable))

    override fun onBindViewHolder(item: InfoClickableItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: InfoClickableItem

        init {
            itemView.container.onClick { navigationCallback.invoke(item.type, item.id) }
        }

        fun bind(item: InfoClickableItem) {
            this.item = item
            with(itemView) {
                infoView.text = item.description
                categoryView.text = item.category
            }
        }

    }
}
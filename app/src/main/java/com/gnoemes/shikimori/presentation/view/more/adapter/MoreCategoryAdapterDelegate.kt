package com.gnoemes.shikimori.presentation.view.more.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.more.MoreCategory
import com.gnoemes.shikimori.entity.more.MoreCategoryItem
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_more_category.view.*

class MoreCategoryAdapterDelegate(
        private val callback: (MoreCategory) -> Unit
) : AbsListItemAdapterDelegate<MoreCategoryItem, Any, MoreCategoryAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is MoreCategoryItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_more_category))

    override fun onBindViewHolder(item: MoreCategoryItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: MoreCategoryItem

        init {
            itemView.container.onClick { callback.invoke(item.category) }
        }

        fun bind(item: MoreCategoryItem) {
            this.item = item
            with(itemView) {
                icon.setImageResource(item.icon)
                categoryView.setText(item.text)
            }
        }

    }
}
package com.gnoemes.shikimori.presentation.view.search.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_search.view.*

class SearchItemAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<SearchItem, Any, SearchItemAdapterDelegate.ViewHolder>() {


    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is SearchItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_search))

    override fun onBindViewHolder(item: SearchItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: SearchItem

        init {
            itemView.cardView.onClick { callback.invoke(item.type, item.id) }
        }

        fun bind(item: SearchItem) {
            this.item = item
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.image.original)
                nameView.text = item.name
                typeView.text = item.typeText
                typeView.visibleIf { !item.typeText.isNullOrEmpty() }
            }
        }

    }
}
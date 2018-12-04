package com.gnoemes.shikimori.presentation.view.common.adapter.content

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_content.view.*

abstract class BaseContentAdapterDelegate(
        private val imageLoader: ImageLoader
) : AbsListItemAdapterDelegate<ContentItem, ContentItem, BaseContentAdapterDelegate.ContentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ContentHolder =
            ContentHolder(parent.inflate(R.layout.item_content))

    override fun onBindViewHolder(item: ContentItem, holder: ContentHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    abstract fun onClick(item: ContentItem)

    inner class ContentHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: ContentItem

        init {
            itemView.cardView.onClick { onClick(item) }
        }

        fun bind(item: ContentItem) {
            this.item = item
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.image.original)
                nameView.text = item.name

                typeView.visibleIf { !item.typeText.isNullOrEmpty() }
                typeView.text = item.typeText

                desctiptionView.visibleIf { !item.description.isNullOrEmpty() }
                desctiptionView.text = item.description
            }
        }
    }
}
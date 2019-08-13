package com.gnoemes.shikimori.presentation.view.common.adapter.content

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import de.hdodenhof.circleimageview.CircleImageView

abstract class BaseContentAdapterDelegate(
        private val imageLoader: ImageLoader
) : AbsListItemAdapterDelegate<ContentItem, Any, BaseContentAdapterDelegate.ContentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ContentHolder =
            ContentHolder(parent.inflate(R.layout.item_content))

    override fun onBindViewHolder(item: ContentItem, holder: ContentHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is ContentItem && isForViewType(item)

    abstract fun isForViewType(item: ContentItem): Boolean

    abstract fun onClick(item: ContentItem)

    inner class ContentHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: ContentItem

        init {
            itemView.findViewById<View>(R.id.container).onClick { onClick(item) }
        }

        fun bind(item: ContentItem) {
            this.item = item
            with(itemView) {
                findViewById<ImageView>(R.id.imageView)?.let {
                    if (it is CircleImageView) imageLoader.setCircleImage(it, item.image.original)
                    else imageLoader.setImageListItem(it, item.image.original)
                }

                findViewById<TextView>(R.id.nameView).text = item.name

                with(findViewById<TextView>(R.id.descriptionView)) {
                    visibleIf { !item.description.isNullOrEmpty() }
                    text = item.description
                }
            }
        }
    }
}
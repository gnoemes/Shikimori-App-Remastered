package com.gnoemes.shikimori.presentation.view.user.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.user.presentation.UserContentItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class UserContentAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback : (Type, Long) -> Unit,
        private val layoutRes : Int
) : AbsListItemAdapterDelegate<UserContentItem, Any, UserContentAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserContentItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(layoutRes))

    override fun onBindViewHolder(item: UserContentItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        private lateinit var item : UserContentItem

        init {
            imageView.onClick { navigationCallback.invoke(item.type, item.id)}
        }

        fun bind(item: UserContentItem) {
            this.item = item
            if (layoutRes == R.layout.item_profile) imageLoader.setCircleImage(imageView, item.image)
            else imageLoader.setImageWithPlaceHolder(imageView, item.image)
        }

    }
}
package com.gnoemes.shikimori.presentation.view.topic.details.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.comment.presentation.CommentViewModel
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicUserViewHolder
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<CommentViewModel, Any, CommentAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is CommentViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_comment))

    override fun onBindViewHolder(item: CommentViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: CommentViewModel

        private val userHolder by lazy { TopicUserViewHolder(itemView.userLayout, imageLoader, navigationCallback) }

        init {
            itemView.contentView.linkCallback = navigationCallback
        }

        fun bind(item: CommentViewModel) {
            this.item = item
            userHolder.bind(item.userData)
            with(itemView) {
                contentView.setContent(item.content)
            }

        }


    }
}
package com.gnoemes.shikimori.presentation.view.topic.details.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.databinding.ItemCommentBinding
import com.gnoemes.shikimori.entity.comment.presentation.CommentViewModel
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicUserViewHolder
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class CommentAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<CommentViewModel, Any, CommentAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is CommentViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemCommentBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: CommentViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: CommentViewModel

        private val userHolder by lazy { TopicUserViewHolder(binding.userLayout, imageLoader, navigationCallback) }

        init {
            binding.contentView.linkCallback = navigationCallback
        }

        fun bind(item: CommentViewModel) {
            this.item = item
            userHolder.bind(item.userData)
            binding.contentView.setContent(item.content)
        }
    }
}
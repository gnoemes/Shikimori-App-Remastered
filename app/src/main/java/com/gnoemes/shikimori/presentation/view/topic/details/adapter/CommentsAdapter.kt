package com.gnoemes.shikimori.presentation.view.topic.details.adapter

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.comment.presentation.CommentViewModel
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.PlaceholderItem
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.utils.images.ImageLoader

class CommentsAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit
) : BasePaginationAdapter() {

    init {
        delegatesManager.addDelegate(CommentAdapterDelegate(imageLoader, navigationCallback))
        delegatesManager.addDelegate(CommentPlaceholderAdapterDelegate())
    }

    fun bindItems(newItems: List<Any>, previous: Boolean) {
        super.bindItems(if (!previous) newItems.takeLast(Constants.DEFAULT_LIMIT) else newItems)
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is CommentViewModel && newItem is CommentViewModel -> oldItem.id == newItem.id
            else -> oldItem is ProgressItem && newItem is ProgressItem
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is CommentViewModel && newItem is CommentViewModel -> oldItem == newItem
            else -> false
        }
    }

    fun showEmptyView() {
        items.clear()
        items.add(PlaceholderItem())
        super.bindItems(items)
    }

    fun hideEmptyView() {
        items.clear()
        super.bindItems(items)
    }

}
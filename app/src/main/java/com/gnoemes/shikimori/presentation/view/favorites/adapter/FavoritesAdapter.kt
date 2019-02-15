package com.gnoemes.shikimori.presentation.view.favorites.adapter

import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.user.presentation.UserContentItem
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.presentation.view.user.adapter.UserContentAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader


class FavoritesAdapter(
        imageLoader: ImageLoader,
        callback: (Type, Long) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.addDelegate(UserContentAdapterDelegate(imageLoader, callback, R.layout.item_favorite))
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is UserContentItem && newItem is UserContentItem -> oldItem.id == newItem.id && oldItem.type == newItem.type
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is UserContentItem && newItem is UserContentItem -> oldItem == newItem
        else -> false
    }
}
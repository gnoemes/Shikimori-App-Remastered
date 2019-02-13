package com.gnoemes.shikimori.presentation.view.clubs.adapter

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.user.presentation.FriendViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.utils.images.ImageLoader

class UserClubAdapter(
        imageLoader: ImageLoader,
        callback: (Type, Long) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.addDelegate(UserClubAdapterDelegate(imageLoader, callback))
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FriendViewModel && newItem is FriendViewModel -> oldItem.id == newItem.id
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is FriendViewModel && newItem is FriendViewModel -> oldItem == newItem
        else -> false
    }
}
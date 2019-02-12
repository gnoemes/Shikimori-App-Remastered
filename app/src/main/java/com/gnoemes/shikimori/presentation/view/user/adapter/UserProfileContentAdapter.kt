package com.gnoemes.shikimori.presentation.view.user.adapter

import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.user.presentation.UserContentItem
import com.gnoemes.shikimori.entity.user.presentation.UserContentMoreItem
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.utils.images.ImageLoader

class UserProfileContentAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit,
        actionCallback: (UserProfileAction) -> Unit
) : BaseUserContentAdapter() {

    init {
        delegatesManager.apply {
            addDelegate(UserContentAdapterDelegate(imageLoader, navigationCallback, R.layout.item_profile))
            addDelegate(UserContentMoreAdapterDelegate(actionCallback, R.layout.item_profile_more))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean =
            when {
                oldItem is UserContentItem && newItem is UserContentItem -> oldItem.id == newItem.id && oldItem.type == newItem.type
                oldItem is UserContentMoreItem && newItem is UserContentMoreItem -> oldItem.type == newItem.type
                else -> false
            }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean =
            when {
                oldItem is UserContentItem && newItem is UserContentItem -> oldItem == newItem
                oldItem is UserContentMoreItem && newItem is UserContentMoreItem -> oldItem == newItem
                else -> false
            }
}
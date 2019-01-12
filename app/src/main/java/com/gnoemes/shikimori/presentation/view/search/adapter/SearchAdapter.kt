package com.gnoemes.shikimori.presentation.view.search.adapter

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ProgressItem
import com.gnoemes.shikimori.entity.search.presentation.SearchItem
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.utils.images.ImageLoader

class SearchAdapter(
        imageLoader: ImageLoader,
        callback: (Type, Long) -> Unit
) : BasePaginationAdapter() {

    init {
        delegatesManager.apply {
            addDelegate(SearchItemAdapterDelegate(imageLoader, callback))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is SearchItem && newItem is SearchItem -> oldItem.id == newItem.id && oldItem.type == newItem.type
            else -> oldItem is ProgressItem && newItem is ProgressItem
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is SearchItem && newItem is SearchItem -> oldItem == newItem
            else -> false
        }
    }
}
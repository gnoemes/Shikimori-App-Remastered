package com.gnoemes.shikimori.presentation.view.similar.adapter

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.similar.presentation.SimilarViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.utils.images.ImageLoader

class SimilarAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit,
        callback: (Long) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(SimilarAdapterDelegate(imageLoader, navigationCallback, callback))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is SimilarViewModel && newItem is SimilarViewModel -> oldItem.id == newItem.id
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is SimilarViewModel && newItem is SimilarViewModel -> oldItem == newItem
        else -> false
    }
}
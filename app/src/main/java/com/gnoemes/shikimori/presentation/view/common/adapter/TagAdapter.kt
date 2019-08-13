package com.gnoemes.shikimori.presentation.view.common.adapter

import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsTagItem

class TagAdapter(
        private val callback: (DetailsAction) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(StudioTagAdapterDelegate(callback))
            addDelegate(GenreTagAdapterDelegate(callback))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is DetailsTagItem && newItem is DetailsTagItem -> oldItem.id == newItem.id && oldItem.type == newItem.type
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is DetailsTagItem && newItem is DetailsTagItem -> oldItem == newItem
        else -> false
    }
}
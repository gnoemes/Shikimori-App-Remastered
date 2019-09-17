package com.gnoemes.shikimori.presentation.view.chronology.adapter

import com.gnoemes.shikimori.entity.chronology.ChronologyViewModel
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.utils.images.ImageLoader

class ChronologyAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit,
        callback: (Long) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.addDelegate(ChronologyAdapterDelegate(imageLoader, navigationCallback, callback))
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is ChronologyViewModel && newItem is ChronologyViewModel -> oldItem.id == newItem.id
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is ChronologyViewModel && newItem is ChronologyViewModel -> oldItem.id == newItem.id
        else -> false
    }
}
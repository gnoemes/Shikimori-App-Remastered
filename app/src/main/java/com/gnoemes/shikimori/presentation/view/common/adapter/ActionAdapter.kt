package com.gnoemes.shikimori.presentation.view.common.adapter

import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsActionType

class ActionAdapter(
        callback : (DetailsAction) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.addDelegate(ActionAdapterDelegate(callback))
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is DetailsActionType && newItem is DetailsActionType -> oldItem == newItem
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is DetailsActionType && newItem is DetailsActionType -> oldItem == newItem
        else -> false
    }
}
package com.gnoemes.shikimori.presentation.view.more.adapter

import com.gnoemes.shikimori.entity.more.MoreCategory
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class MoreAdapter(
        private val callback: (MoreCategory) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        delegatesManager.apply {
            addDelegate(MoreProfileAdapterDelegate(callback))
            addDelegate(MoreCategoryAdapterDelegate(callback))
        }

        items = mutableListOf()
    }

    fun bindItems(newItems: List<Any>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
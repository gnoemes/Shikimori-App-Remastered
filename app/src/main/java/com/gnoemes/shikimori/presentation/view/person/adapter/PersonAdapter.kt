package com.gnoemes.shikimori.presentation.view.person.adapter

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.common.adapter.DetailsOldContentMockAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class PersonAdapter(
        imageLoader: ImageLoader,
        navigationCallback: (Type, Long) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {
    init {
        with(delegatesManager) {
            addDelegate(PersonHeadAdapterDelegate(imageLoader))
            addDelegate(PersonDescriptionAdapterDelegate())
            addDelegate(DetailsOldContentMockAdapterDelegate(imageLoader,  navigationCallback, null))
        }
        items = mutableListOf()
    }

    fun bindItems(it: List<Any>) {
        items.clear()
        items.addAll(it)
        notifyDataSetChanged()
    }

}

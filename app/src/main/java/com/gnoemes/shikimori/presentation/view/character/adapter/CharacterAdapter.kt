package com.gnoemes.shikimori.presentation.view.character.adapter

import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.view.common.adapter.DetailsContentAdapterDelegate
import com.gnoemes.shikimori.presentation.view.common.adapter.DetailsDescriptionAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class CharacterAdapter(
        imageLoader: ImageLoader,
        settings: SettingsSource,
        navigationCallback: (Type, Long) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {
    init {
        with(delegatesManager) {
            addDelegate(CharacterHeadAdapterDelegate(imageLoader))
            addDelegate(DetailsDescriptionAdapterDelegate())
            addDelegate(DetailsContentAdapterDelegate(imageLoader, settings, navigationCallback, null))
        }
        items = mutableListOf()
    }

    fun bindItems(it: List<Any>) {
        items.clear()
        items.addAll(it)
        notifyDataSetChanged()
    }

}
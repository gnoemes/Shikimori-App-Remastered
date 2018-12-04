package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentCharacterAdapterDelegate(
        imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : BaseContentAdapterDelegate(imageLoader) {

    override fun isForViewType(item: ContentItem, items: MutableList<ContentItem>, position: Int): Boolean = item.raw is Character

    override fun onClick(item: ContentItem) {
        val raw = item.raw as Character
        navigationCallback.invoke(raw.linkedType, raw.id)
    }
}
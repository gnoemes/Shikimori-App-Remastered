package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentRelatedAdapterDelegate(
        imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : BaseContentAdapterDelegate(imageLoader) {

    override fun isForViewType(item: ContentItem, items: MutableList<ContentItem>, position: Int): Boolean = item.raw is Related

    override fun onClick(item: ContentItem) {
        val raw = item.raw as Related

        val rawItem = when (raw.type) {
            Type.ANIME -> raw.anime!!
            else -> raw.manga!!
        }

        navigationCallback.invoke(rawItem.linkedType, rawItem.linkedId)
    }
}
package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentPersonAdapterDelegate(
        imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : BaseContentAdapterDelegate(imageLoader) {

    override fun isForViewType(item: ContentItem): Boolean = item.raw is Person

    override fun onClick(item: ContentItem) {
        val raw = item.raw as Person
        navigationCallback.invoke(raw.linkedType, raw.id)
    }
}
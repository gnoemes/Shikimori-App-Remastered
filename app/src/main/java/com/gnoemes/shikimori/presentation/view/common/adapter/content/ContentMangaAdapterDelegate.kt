package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentMangaAdapterDelegate(
        imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : BaseContentAdapterDelegate(imageLoader) {

    override fun isForViewType(item: ContentItem, items: MutableList<ContentItem>, position: Int): Boolean = item.raw is Manga

    override fun onClick(item: ContentItem) {
        val raw = item.raw as Manga
        navigationCallback.invoke(raw.linkedType, raw.id)
    }
}
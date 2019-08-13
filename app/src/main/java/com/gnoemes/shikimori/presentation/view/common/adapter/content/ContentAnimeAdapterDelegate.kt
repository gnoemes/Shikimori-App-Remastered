package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentAnimeAdapterDelegate(
        imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : BaseContentAdapterDelegate(imageLoader) {

    override fun isForViewType(item: ContentItem): Boolean = item.raw is Anime

    override fun onClick(item: ContentItem) {
        val raw = item.raw as Anime
        navigationCallback.invoke(raw.linkedType, raw.id)
    }
}
package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentVideoAdapterDelegate(
        imageLoader: ImageLoader,
        private val detailsCallback: ((DetailsAction) -> Unit)?
) : BaseContentAdapterDelegate(imageLoader) {

    override fun isForViewType(item: ContentItem, items: MutableList<ContentItem>, position: Int): Boolean = item.raw is AnimeVideo

    override fun onClick(item: ContentItem) {
        val raw = item.raw as AnimeVideo
        detailsCallback?.invoke(DetailsAction.Video(raw.url))
    }
}
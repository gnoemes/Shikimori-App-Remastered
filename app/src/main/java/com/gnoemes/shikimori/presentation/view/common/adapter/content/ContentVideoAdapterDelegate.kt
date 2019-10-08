package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.FrameItem
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentVideoAdapterDelegate(
        imageLoader: ImageLoader,
        private val detailsCallback: ((DetailsAction) -> Unit)?
) : BaseFrameContentAdaterDelegate(imageLoader) {

    override fun isForViewType(item: FrameItem): Boolean = item.raw is AnimeVideo

    override fun onClick(item: FrameItem) {
        val raw = item.raw as AnimeVideo
        detailsCallback?.invoke(DetailsAction.Video(raw.url))
    }
}
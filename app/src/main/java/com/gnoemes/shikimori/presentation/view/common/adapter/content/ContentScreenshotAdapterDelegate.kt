package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.anime.domain.Screenshot
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.FrameItem
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentScreenshotAdapterDelegate(
        imageLoader: ImageLoader,
        private val detailsCallback: ((DetailsAction) -> Unit)?
) : BaseFrameContentAdaterDelegate(imageLoader) {

    override fun isForViewType(item: FrameItem): Boolean = item.raw is Screenshot

    override fun onClick(item: FrameItem) {}

    override fun onClickIndexed(item: FrameItem, pos: Int) {
        detailsCallback?.invoke(DetailsAction.Screenshots(pos))
    }
}
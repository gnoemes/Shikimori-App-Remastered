package com.gnoemes.shikimori.presentation.view.common.adapter.content

import android.view.ViewGroup
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate

class ContentCharacterAdapterDelegate(
        imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : BaseContentAdapterDelegate(imageLoader) {

    override fun isForViewType(item: ContentItem): Boolean = item.raw is Character

    override fun onCreateViewHolder(parent: ViewGroup): ContentHolder =
            ContentHolder(parent.inflate(R.layout.item_content_circle))

    override fun onClick(item: ContentItem) {
        val raw = item.raw as Character
        navigationCallback.invoke(raw.linkedType, raw.id)
    }
}
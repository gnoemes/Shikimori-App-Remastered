package com.gnoemes.shikimori.presentation.view.topic.holders

import android.graphics.drawable.ColorDrawable
import android.view.View
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.presentation.TopicUserViewModel
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.layout_topic_user.view.*

class TopicUserViewHolder(
        private val view: View,
        private val imageLoader: ImageLoader,
        private val navigationCallback: ((Type, Long) -> Unit)? = null
) {
    private lateinit var item: TopicUserViewModel

    init {
        view.userInfoGroup.setOnClickListener { navigationCallback?.invoke(Type.USER, item.user.id) }
    }

    fun bind(item: TopicUserViewModel) {
        this.item = item
        with(view) {
            imageLoader.setCircleImage(avatarView, item.user.avatar)
            nameView.text = item.user.nickname
            dateView.text = item.createdDate

            tagView.text = item.tag
            item.tagColor?.let { tagView.background = ColorDrawable(item.tagColor) }
            tagView.visibleIf { !item.tag.isNullOrBlank() }
        }
    }
}
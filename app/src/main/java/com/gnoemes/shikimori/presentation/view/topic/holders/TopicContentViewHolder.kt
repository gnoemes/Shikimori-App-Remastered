package com.gnoemes.shikimori.presentation.view.topic.holders

import android.view.View
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.presentation.TopicContentViewModel
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.layout_topic.view.*

class TopicContentViewHolder(
        private val view: View,
        navigationCallback: (Type, Long) -> Unit,
        private val expandable: Boolean = false
) {

    private lateinit var item: TopicContentViewModel

    init {
        view.contentView.expandable = expandable
        view.contentView.linkCallback = navigationCallback
    }

    fun bind(item: TopicContentViewModel) {
        this.item = item
        with(view) {
            titleView.text = item.title
            contentView.visibleIf { !item.content.isNullOrBlank() }
            contentView.setContent(item.content)
        }
    }
}
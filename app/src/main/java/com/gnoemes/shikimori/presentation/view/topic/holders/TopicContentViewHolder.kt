package com.gnoemes.shikimori.presentation.view.topic.holders

import com.gnoemes.shikimori.databinding.LayoutTopicBinding
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.presentation.TopicContentViewModel
import com.gnoemes.shikimori.utils.visibleIf

class TopicContentViewHolder(
        private val binding: LayoutTopicBinding,
        navigationCallback: (Type, Long) -> Unit,
        private val expandable: Boolean = false
) {

    private lateinit var item: TopicContentViewModel

    init {
        binding.contentView.expandable = expandable
        binding.contentView.linkCallback = navigationCallback
    }

    fun bind(item: TopicContentViewModel) {
        this.item = item
        binding.titleView.text = item.title
        binding.contentView.visibleIf { !item.content.isNullOrBlank() }
        binding.contentView.setContent(item.content)
    }
}
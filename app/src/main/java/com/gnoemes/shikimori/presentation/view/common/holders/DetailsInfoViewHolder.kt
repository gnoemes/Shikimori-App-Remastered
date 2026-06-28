package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.databinding.LayoutDetailsInfoBinding
import com.gnoemes.shikimori.entity.common.presentation.DetailsInfoItem
import com.gnoemes.shikimori.presentation.view.common.adapter.InfoAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.TagAdapter
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import com.gnoemes.shikimori.utils.widgets.InfoSpaceDecorator


class DetailsInfoViewHolder(
        private val binding: LayoutDetailsInfoBinding,
        private val tagsAdapter: TagAdapter,
        private val infoAdapter: InfoAdapter
) {

    private val placeholder: DetailsPlaceholderViewHolder by lazy { DetailsPlaceholderViewHolder(binding.infoContent.root, binding.infoPlaceholder.root) }

    init {
        with(binding.infoContent) {
            tagList.apply {
                adapter = tagsAdapter
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(root.context, RecyclerView.HORIZONTAL, false)
                addItemDecoration(HorizontalSpaceItemDecorator(root.context.dp(8), root.context.dp(16)))
                setHasFixedSize(true)
            }
            infoList.apply {
                adapter = infoAdapter
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(root.context, RecyclerView.HORIZONTAL, false)
                addItemDecoration(InfoSpaceDecorator(root.context.dp(32), root.context.dp(24), root.context.dp(8), root.context.dp(24)))
                setHasFixedSize(true)
            }
        }
    }

    fun bind(item: DetailsInfoItem) {
        placeholder.showContent()

        with(binding.infoContent) {
            nameSecondView.text = item.nameSecond
            tagsAdapter.bindItems(item.tags)
            infoAdapter.bindItems(item.info)
        }
    }
}
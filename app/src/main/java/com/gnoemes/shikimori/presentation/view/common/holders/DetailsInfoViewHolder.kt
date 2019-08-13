package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.entity.common.presentation.DetailsInfoItem
import com.gnoemes.shikimori.presentation.view.common.adapter.InfoAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.TagAdapter
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import com.gnoemes.shikimori.utils.widgets.InfoSpaceDecorator
import kotlinx.android.synthetic.main.layout_details_info.view.*
import kotlinx.android.synthetic.main.layout_details_info_content.view.*


class DetailsInfoViewHolder(
        private val view: View,
        private val tagsAdapter: TagAdapter,
        private val infoAdapter: InfoAdapter
) {

    private val placeholder: DetailsPlaceholderViewHolder by lazy { DetailsPlaceholderViewHolder(view.infoContent, view.infoPlaceholder as ShimmerFrameLayout) }

    init {
        with(view.infoContent) {
            tagList.apply {
                adapter = tagsAdapter
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                addItemDecoration(HorizontalSpaceItemDecorator(context.dp(8), context.dp(16)))
                setHasFixedSize(true)
            }
            infoList.apply {
                adapter = infoAdapter
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                addItemDecoration(InfoSpaceDecorator(context.dp(32), context.dp(24), context.dp(8), context.dp(24)))
                setHasFixedSize(true)
            }
        }
    }

    fun bind(item: DetailsInfoItem) {
        placeholder.showContent()

        with(view.infoContent) {
            nameSecondView.text = item.nameSecond
            tagsAdapter.bindItems(item.tags)
            infoAdapter.bindItems(item.info)
        }
    }
}
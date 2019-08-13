package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.entity.common.presentation.DetailsActionItem
import com.gnoemes.shikimori.presentation.view.common.adapter.ActionAdapter
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import kotlinx.android.synthetic.main.layout_details_action.view.*

class DetailsActionViewHolder(
        private val view: View,
        private val actionAdapter: ActionAdapter
) {

    private val placeholder: DetailsPlaceholderViewHolder by lazy { DetailsPlaceholderViewHolder(view.actionContent, view.actionPlaceholder as ShimmerFrameLayout) }

    init {
        with(view.actionContent as RecyclerView) {
            adapter = actionAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(HorizontalSpaceItemDecorator(context.dp(8), context.dp(16)))
            setHasFixedSize(true)
        }
    }

    fun bind(item: DetailsActionItem) {
        placeholder.showContent()

        actionAdapter.bindItems(item.actions)
    }
}
package com.gnoemes.shikimori.presentation.view.user.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.UserContentMoreItem
import com.gnoemes.shikimori.entity.user.presentation.UserContentType
import com.gnoemes.shikimori.entity.user.presentation.UserContentViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.StartSnapHelper
import com.gnoemes.shikimori.presentation.view.user.adapter.BaseUserContentAdapter
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import kotlinx.android.synthetic.main.layout_user_profile_content.view.*

class UserContentViewHolder(
        private val view: View,
        private val adapter: BaseUserContentAdapter
) {

    init {
        val snapOffset = view.resources.getDimension(R.dimen.margin_normal).toInt()
        val snapHelper = StartSnapHelper(snapOffset)
        snapHelper.attachToRecyclerView(view.contentRecyclerView)

        view.contentRecyclerView.apply {
            adapter = this@UserContentViewHolder.adapter.apply { if (!hasObservers()) setHasStableIds(true) }
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
            setHasFixedSize(true)
            val spacing = resources.getDimension(R.dimen.margin_normal).toInt()
            val firstItemSpacing = resources.getDimension(R.dimen.margin_big).toInt()
            addItemDecoration(HorizontalSpaceItemDecorator(spacing, firstItemSpacing))
        }
    }

    fun bind(item: UserContentViewModel) {
        val items = mutableListOf<Any>()
        items.addAll(item.content)

        if (item.needShowMore) items.add(UserContentMoreItem(item.type, item.moreSize))
        adapter.bindItems(items)

        val stringRes = when(item.type) {
            UserContentType.FAVORITES -> R.string.common_favorite
            UserContentType.CLUBS -> R.string.common_clubs
            UserContentType.FRIENDS -> R.string.common_friends
        }

        view.contentLabelView.setText(stringRes)
    }
}
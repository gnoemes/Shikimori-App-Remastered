package com.gnoemes.shikimori.presentation.view.user.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.LayoutUserProfileContentBinding
import com.gnoemes.shikimori.entity.user.presentation.UserContentMoreItem
import com.gnoemes.shikimori.entity.user.presentation.UserContentType
import com.gnoemes.shikimori.entity.user.presentation.UserContentViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.StartSnapHelper
import com.gnoemes.shikimori.presentation.view.user.adapter.BaseUserContentAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.visible
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator

class UserContentViewHolder(
        private val binding: LayoutUserProfileContentBinding,
        private val adapter: BaseUserContentAdapter
) {

    init {
        if (binding.contentRecyclerView.onFlingListener == null) {
            val snapOffset = binding.root.resources.getDimension(R.dimen.margin_normal).toInt()
            val snapHelper = StartSnapHelper(snapOffset)
            snapHelper.attachToRecyclerView(binding.contentRecyclerView)
        }

        binding.contentRecyclerView.apply {
            adapter = this@UserContentViewHolder.adapter.apply { if (!hasObservers()) setHasStableIds(true) }
            layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
            setHasFixedSize(true)
            val spacing = binding.root.resources.getDimension(R.dimen.margin_normal).toInt()
            val firstItemSpacing = binding.root.resources.getDimension(R.dimen.margin_big).toInt()
            addItemDecoration(HorizontalSpaceItemDecorator(spacing, firstItemSpacing))
        }
    }

    fun bind(item: UserContentViewModel) {
        if (item.content.isEmpty()) {
            binding.root.gone()
            return
        }

        val items = mutableListOf<Any>()
        items.addAll(item.content)

        if (item.needShowMore) items.add(UserContentMoreItem(item.type, item.moreSize))
        adapter.bindItems(items)

        val stringRes = when (item.type) {
            UserContentType.FAVORITES -> R.string.common_favorite
            UserContentType.CLUBS -> R.string.common_clubs
            UserContentType.FRIENDS -> R.string.common_friends
        }

        with(binding) {
            contentLabelView.setText(stringRes)
            progressBar.gone()
            contentRecyclerView.visible()
        }
    }

    companion object {
        fun create(view: View, adapter: BaseUserContentAdapter): UserContentViewHolder {
            return UserContentViewHolder(LayoutUserProfileContentBinding.bind(view), adapter)
        }
    }
}
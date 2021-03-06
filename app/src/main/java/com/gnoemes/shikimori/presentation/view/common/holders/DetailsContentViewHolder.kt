package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.presentation.view.common.adapter.StartSnapHelper
import com.gnoemes.shikimori.presentation.view.common.adapter.content.ContentAdapter
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.visible
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import kotlinx.android.synthetic.main.layout_details_content.view.contentLabelView
import kotlinx.android.synthetic.main.layout_details_content.view.contentRecyclerView
import kotlinx.android.synthetic.main.layout_details_content.view.progressBar
import kotlinx.android.synthetic.main.layout_details_content_with_search.view.*

class DetailsContentViewHolder(
        private val view: View,
        private val adapter: ContentAdapter,
        private val withSearch : Boolean= false
) {

    init {
        if (view.contentRecyclerView.onFlingListener == null) {
            val snapOffset = view.resources.getDimension(R.dimen.margin_normal).toInt()
            val snapHelper = StartSnapHelper(snapOffset)
            snapHelper.attachToRecyclerView(view.contentRecyclerView)
        }

        view.contentRecyclerView.apply {
            adapter = this@DetailsContentViewHolder.adapter.apply { if (!hasObservers()) setHasStableIds(true) }
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
            setHasFixedSize(true)
            addItemDecoration(HorizontalSpaceItemDecorator(context.dp(if (withSearch) 16 else 8), context.dp(16)))
        }
    }

    fun bind(type: DetailsContentType, item: DetailsContentItem) {
        if (item.items.isEmpty()) {
            view.gone()
            return
        }

        val stringRes = when (type) {
            DetailsContentType.CHARACTERS -> R.string.common_characters
            DetailsContentType.RELATED -> R.string.common_related
            DetailsContentType.SIMILAR -> R.string.common_similar
            DetailsContentType.VIDEO -> R.string.common_video
            DetailsContentType.MANGAS -> R.string.common_manga
            DetailsContentType.ANIMES -> R.string.common_anime
            DetailsContentType.SEYUS -> R.string.common_seyu
            DetailsContentType.WORKS -> R.string.person_best_works
            DetailsContentType.ROLES -> R.string.person_best_roles
            DetailsContentType.SCREENSHOTS -> R.string.details_screenshots
        }

        with(view) {
            contentLabelView.setText(stringRes)
            adapter.bindItems(item.items)
            if (withSearch) searchBtn.visible()
            progressBar.gone()
            contentRecyclerView.visible()
        }
    }
}
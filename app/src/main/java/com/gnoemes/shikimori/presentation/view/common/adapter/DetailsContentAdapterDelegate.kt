package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.visible
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_details_content.view.*

class DetailsContentAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val settings: SettingsSource,
        private val navigationCallback: (Type, Long) -> Unit,
        private val detailsCallback: ((DetailsAction) -> Unit)?
) : AbsListItemAdapterDelegate<DetailsContentItem, Any, DetailsContentAdapterDelegate.ViewHolder>() {
    private val pool = RecyclerView.RecycledViewPool()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is DetailsContentItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_details_content))

    override fun onBindViewHolder(item: DetailsContentItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val contentAdapter by lazy { DetailsContentAdapter(imageLoader, settings, navigationCallback, detailsCallback) }

        fun bind(item: DetailsContentItem) {
            when (item) {
                is DetailsContentItem.Loading -> onLoading()
                is DetailsContentItem.Content -> onContent(item)
            }

            val stringRes = when (item.type) {
                DetailsContentType.CHARACTERS -> R.string.common_characters
                DetailsContentType.RELATED -> R.string.common_related
                DetailsContentType.SIMILAR -> R.string.common_similar
                DetailsContentType.VIDEO -> R.string.common_video
                DetailsContentType.MANGAS -> R.string.common_manga
                DetailsContentType.ANIMES -> R.string.common_anime
                DetailsContentType.SEYUS -> R.string.common_seyu
            }

            itemView.contentLabelView.setText(stringRes)
        }

        private fun onContent(item: DetailsContentItem.Content) {
            TransitionManager.beginDelayedTransition(itemView.container, AutoTransition())
            with(itemView) {
                progressBar.gone()
                contentRecyclerView.visible()

                contentRecyclerView.apply {
                    adapter = contentAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 4 }
                    setRecycledViewPool(pool)
                }



                contentAdapter.bindItems(item.items)
            }
        }

        private fun onLoading() {
            TransitionManager.beginDelayedTransition(itemView.container, AutoTransition())
            with(itemView) {
                progressBar.visible()
                contentRecyclerView.gone()
            }
        }

    }
}
package com.gnoemes.shikimori.presentation.view.common.adapter.content

import androidx.recyclerview.widget.DiffUtil
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.roles.domain.Work
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class ContentAdapter(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit,
        private val detailsCallback: ((DetailsAction) -> Unit)?
) : ListDelegationAdapter<MutableList<ContentItem>>() {

    init {
        delegatesManager.apply {
            addDelegate(ContentAnimeAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentMangaAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentCharacterAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentPersonAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentRelatedAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentVideoAdapterDelegate(imageLoader, detailsCallback))
            addDelegate(ContentWorkAdapterDelegate(imageLoader, navigationCallback))
        }
        setItems(mutableListOf())
    }

    fun bindItems(newItems: List<ContentItem>) {
        val oldData = items.toMutableList()
        items.clear()
        items.addAll(newItems)

        DiffUtil.calculateDiff(DiffCallback(oldData, items))
                .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
            private val oldItems: MutableList<ContentItem>,
            private val newItems: MutableList<ContentItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return when {
                oldItem.raw is Anime && newItem.raw is Anime -> oldItem.raw.id == newItem.raw.id
                oldItem.raw is Manga && newItem.raw is Manga -> oldItem.raw.id == newItem.raw.id
                oldItem.raw is Character && newItem.raw is Character -> oldItem.raw.id == newItem.raw.id
                oldItem.raw is Person && newItem.raw is Person -> oldItem.raw.id == newItem.raw.id
                oldItem.raw is Related && newItem.raw is Related -> {
                    if (oldItem.raw.anime != null && newItem.raw.anime != null) oldItem.raw.anime.id == newItem.raw.anime.id
                    else oldItem.raw.manga!!.id == newItem.raw.manga!!.id
                }
                oldItem.raw is AnimeVideo && newItem.raw is AnimeVideo -> oldItem.raw.id == newItem.raw.id
                oldItem.raw is Work && newItem.raw is Work -> {
                    if (oldItem.raw.anime != null && newItem.raw.anime != null) oldItem.raw.anime.id == newItem.raw.anime.id
                    else oldItem.raw.manga!!.id == newItem.raw.manga!!.id
                }
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem == newItem
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }
}
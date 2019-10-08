package com.gnoemes.shikimori.presentation.view.common.adapter.content

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.ContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.FrameItem
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.roles.domain.Work
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.presentation.view.common.adapter.CharacterSearchPlaceholderAdapterDelegate
import com.gnoemes.shikimori.utils.images.ImageLoader

class ContentAdapter(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit,
        private val detailsCallback: ((DetailsAction) -> Unit)? = null
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(ContentAnimeAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentMangaAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentCharacterAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentPersonAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentRelatedAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentVideoAdapterDelegate(imageLoader, detailsCallback))
            addDelegate(ContentWorkAdapterDelegate(imageLoader, navigationCallback))
            addDelegate(ContentScreenshotAdapterDelegate(imageLoader, detailsCallback))

            addDelegate(CharacterSearchPlaceholderAdapterDelegate())
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is ContentItem && oldItem.raw is Anime && newItem is ContentItem && newItem.raw is Anime -> oldItem.raw.id == newItem.raw.id
        oldItem is ContentItem && oldItem.raw is Manga && newItem is ContentItem && newItem.raw is Manga -> oldItem.raw.id == newItem.raw.id
        oldItem is ContentItem && oldItem.raw is Character && newItem is ContentItem && newItem.raw is Character -> oldItem.raw.id == newItem.raw.id
        oldItem is ContentItem && oldItem.raw is Person && newItem is ContentItem && newItem.raw is Person -> oldItem.raw.id == newItem.raw.id
        oldItem is ContentItem && oldItem.raw is Related && newItem is ContentItem && newItem.raw is Related -> {
            if (oldItem.raw.anime != null && newItem.raw.anime != null) oldItem.raw.anime.id == newItem.raw.anime.id
            else oldItem.raw.manga!!.id == newItem.raw.manga!!.id
        }
        oldItem is FrameItem && oldItem.raw is AnimeVideo && newItem is FrameItem && newItem.raw is AnimeVideo -> oldItem.raw.id == newItem.raw.id
        oldItem is ContentItem && oldItem.raw is Work && newItem is ContentItem && newItem.raw is Work -> {
            if (oldItem.raw.anime != null && newItem.raw.anime != null) oldItem.raw.anime.id == newItem.raw.anime.id
            else oldItem.raw.manga!!.id == newItem.raw.manga!!.id
        }
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem

}
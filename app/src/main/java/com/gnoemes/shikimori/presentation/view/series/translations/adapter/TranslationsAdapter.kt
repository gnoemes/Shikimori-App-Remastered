package com.gnoemes.shikimori.presentation.view.series.translations.adapter

import com.gnoemes.shikimori.entity.series.domain.TranslationMenu
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter
import com.gnoemes.shikimori.presentation.view.series.episodes.adapter.SeriesPlaceholderAdapterDelegate

class TranslationsAdapter(
        callback: (TranslationVideo) -> Unit,
        menuListener: (TranslationMenu) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(TranslationAdapterDelegate(callback, menuListener))
            addDelegate(SeriesPlaceholderAdapterDelegate())
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is TranslationViewModel && newItem is TranslationViewModel -> oldItem.firstVideoId == newItem.firstVideoId
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is TranslationViewModel && newItem is TranslationViewModel -> oldItem == newItem
        else -> false
    }
}
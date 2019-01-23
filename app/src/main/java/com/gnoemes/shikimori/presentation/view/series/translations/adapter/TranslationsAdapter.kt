package com.gnoemes.shikimori.presentation.view.series.translations.adapter

import com.gnoemes.shikimori.entity.series.domain.TranslationMenu
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.BaseAdapter

class TranslationsAdapter(
        callback: (TranslationVideo) -> Unit,
        menuListener: (TranslationMenu) -> Unit
) : BaseAdapter<Any>() {

    init {
        delegatesManager.apply {
            addDelegate(TranslationAdapterDelegate(callback, menuListener))
        }
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is TranslationViewModel && newItem is TranslationViewModel -> oldItem.authors == newItem.authors
        else -> false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = when {
        oldItem is TranslationViewModel && newItem is TranslationViewModel -> oldItem == newItem
        else -> false
    }
}
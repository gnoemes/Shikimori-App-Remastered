package com.gnoemes.shikimori.presentation.presenter.manga.converter

import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsOptionsItem
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails

interface MangaDetailsViewModelConverter {

    fun convertHead(it: MangaDetails): DetailsHeadItem

    fun convertOptions(it: MangaDetails, isGuest: Boolean): DetailsOptionsItem

    fun convertDescriptionItem(description: String?): DetailsDescriptionItem
}
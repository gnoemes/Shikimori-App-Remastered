package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsOptionsItem

interface AnimeDetailsViewModelConverter {

    fun convertHead(it: AnimeDetails): DetailsHeadItem

    fun convertOptions(it: AnimeDetails, isGuest : Boolean): DetailsOptionsItem

}
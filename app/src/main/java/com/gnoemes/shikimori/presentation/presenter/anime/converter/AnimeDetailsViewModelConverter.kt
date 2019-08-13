package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.common.presentation.DetailsActionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsInfoItem
import com.gnoemes.shikimori.entity.roles.domain.Person

interface AnimeDetailsViewModelConverter {

    fun convertHead(it: AnimeDetails, isGuest : Boolean): DetailsHeadItem

    fun convertDescriptionItem(description: String?): DetailsDescriptionItem

    fun convertInfo(it: AnimeDetails, creators: List<Pair<Person, List<String>>>) : DetailsInfoItem

    fun getActions() : DetailsActionItem
}
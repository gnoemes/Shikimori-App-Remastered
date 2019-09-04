package com.gnoemes.shikimori.presentation.presenter.manga.converter

import com.gnoemes.shikimori.entity.common.presentation.DetailsActionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsInfoItem
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.entity.user.domain.Statistic
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem

interface MangaDetailsViewModelConverter {

    fun convertHead(it: MangaDetails, isGuest : Boolean): DetailsHeadItem

    fun convertInfo(it: MangaDetails, creators: List<Pair<Person, List<String>>>) : DetailsInfoItem

    fun convertDescriptionItem(description: String?): DetailsDescriptionItem

    fun getActions() : DetailsActionItem

    fun convertScores(t: List<Statistic>): List<UserStatisticItem>

    fun convertStatuses(t: List<Statistic>): List<UserStatisticItem>
}
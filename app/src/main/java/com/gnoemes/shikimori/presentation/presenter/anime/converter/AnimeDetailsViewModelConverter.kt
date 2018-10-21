package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.roles.domain.Character
import io.reactivex.functions.Function

interface AnimeDetailsViewModelConverter : Function<AnimeDetails, List<Any>> {

    fun convertSimilar(it: List<Anime>): Any

    fun convertRelated(it: List<Related>): Any

    fun convertCharacters(it: List<Character>): Any
}
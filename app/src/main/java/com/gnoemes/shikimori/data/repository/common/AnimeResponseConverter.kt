package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.anime.data.AnimeResponse
import com.gnoemes.shikimori.entity.anime.domain.Anime
import io.reactivex.functions.Function

interface AnimeResponseConverter : Function<List<AnimeResponse?>?, List<Anime>> {

    fun convertResponse(response: AnimeResponse?): Anime?
}
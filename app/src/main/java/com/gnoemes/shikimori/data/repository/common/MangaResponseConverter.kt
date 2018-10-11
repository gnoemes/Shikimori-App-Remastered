package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.gnoemes.shikimori.entity.manga.domain.Manga
import io.reactivex.functions.Function

interface MangaResponseConverter : Function<List<MangaResponse?>, List<Manga>> {

    fun convertResponse(it: MangaResponse?): Manga?
}
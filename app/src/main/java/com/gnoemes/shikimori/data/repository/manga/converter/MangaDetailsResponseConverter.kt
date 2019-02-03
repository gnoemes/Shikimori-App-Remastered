package com.gnoemes.shikimori.data.repository.manga.converter

import com.gnoemes.shikimori.entity.manga.data.MangaDetailsResponse
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import io.reactivex.functions.Function

interface MangaDetailsResponseConverter : Function<MangaDetailsResponse, MangaDetails>
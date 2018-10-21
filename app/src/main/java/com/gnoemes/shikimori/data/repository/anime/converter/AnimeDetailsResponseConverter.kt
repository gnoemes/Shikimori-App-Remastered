package com.gnoemes.shikimori.data.repository.anime.converter

import com.gnoemes.shikimori.entity.anime.data.AnimeDetailsResponse
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import io.reactivex.functions.Function

interface AnimeDetailsResponseConverter : Function<AnimeDetailsResponse, AnimeDetails>
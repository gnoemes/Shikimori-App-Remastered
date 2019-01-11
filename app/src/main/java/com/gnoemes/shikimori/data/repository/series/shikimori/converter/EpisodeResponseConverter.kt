package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.EpisodeResponse
import com.gnoemes.shikimori.entity.series.domain.Episode
import io.reactivex.functions.Function

interface  EpisodeResponseConverter : Function<List<EpisodeResponse>, List<Episode>>
package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.series.domain.Episode
import io.reactivex.functions.Function

interface EpisodeViewModelConverter : Function<List<Episode>, List<Any>>
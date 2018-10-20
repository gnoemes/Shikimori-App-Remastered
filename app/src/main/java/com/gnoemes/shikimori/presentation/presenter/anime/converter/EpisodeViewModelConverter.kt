package com.gnoemes.shikimori.presentation.presenter.anime.converter

import com.gnoemes.shikimori.entity.series.domain.Series
import io.reactivex.functions.Function

interface EpisodeViewModelConverter : Function<Series, List<Any>>
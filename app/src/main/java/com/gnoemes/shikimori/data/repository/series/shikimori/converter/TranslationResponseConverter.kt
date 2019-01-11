package com.gnoemes.shikimori.data.repository.series.shikimori.converter

import com.gnoemes.shikimori.entity.series.data.TranslationResponse
import com.gnoemes.shikimori.entity.series.domain.Translation
import io.reactivex.functions.Function

interface TranslationResponseConverter : Function<List<TranslationResponse>, List<Translation>>
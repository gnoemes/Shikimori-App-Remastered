package com.gnoemes.shikimori.presentation.presenter.common.converter

import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import io.reactivex.functions.Function

interface DetailsContentViewModelConverter : Function<List<Any>, DetailsContentItem>
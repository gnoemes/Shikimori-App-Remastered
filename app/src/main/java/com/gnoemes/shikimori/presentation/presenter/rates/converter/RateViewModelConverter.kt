package com.gnoemes.shikimori.presentation.presenter.rates.converter

import io.reactivex.functions.Function

interface RateViewModelConverter : Function<List<Any>, List<Any>>
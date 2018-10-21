package com.gnoemes.shikimori.presentation.presenter.common.converter

import com.gnoemes.shikimori.entity.common.domain.Link
import io.reactivex.functions.Function

interface LinkViewModelConverter : Function<List<Link>, List<Pair<String, String>>>
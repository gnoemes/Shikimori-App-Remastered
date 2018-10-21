package com.gnoemes.shikimori.presentation.presenter.common.converter

import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import io.reactivex.functions.Function

interface FranchiseNodeViewModelConverter : Function<List<FranchiseNode>, List<Pair<String, String>>>
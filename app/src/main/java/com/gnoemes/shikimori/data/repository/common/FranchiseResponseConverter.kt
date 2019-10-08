package com.gnoemes.shikimori.data.repository.common

import com.gnoemes.shikimori.entity.common.data.FranchiseResponse
import com.gnoemes.shikimori.entity.common.domain.Franchise
import io.reactivex.functions.Function

interface FranchiseResponseConverter : Function<FranchiseResponse, Franchise>
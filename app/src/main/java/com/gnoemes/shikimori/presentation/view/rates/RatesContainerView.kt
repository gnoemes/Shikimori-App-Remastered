package com.gnoemes.shikimori.presentation.view.rates

import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface RatesContainerView : BaseFragmentView {

    fun setData(id: Long, type: Type, it: List<Pair<RateStatus, String>>)
}
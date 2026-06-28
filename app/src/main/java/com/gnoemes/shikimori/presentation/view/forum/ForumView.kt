package com.gnoemes.shikimori.presentation.view.forum

import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView

interface ForumView : BaseNetworkView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(items : List<Forum>)
}
package com.gnoemes.shikimori.presentation.view.clubs

import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface UserClubsView : BaseFragmentView {

    fun showData(items: List<UserClubViewModel>)

    fun showClubsCount(count : Int)
}
package com.gnoemes.shikimori.presentation.view.clubs

import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface UserClubsView : BaseFragmentView {

    fun showData(items: List<UserClubViewModel>)

    fun showClubsCount(count : Int)
}
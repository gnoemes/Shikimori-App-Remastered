package com.gnoemes.shikimori.presentation.view.friends

import com.gnoemes.shikimori.entity.user.presentation.FriendViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface FriendsView : BaseFragmentView {

    fun showData(items: List<FriendViewModel>)

    fun showFriendsCount(count : Int)
}
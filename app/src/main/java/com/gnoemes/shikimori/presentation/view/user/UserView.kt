package com.gnoemes.shikimori.presentation.view.user

import com.gnoemes.shikimori.entity.user.presentation.UserContentViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserHeadViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserInfoViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserRateViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface UserView : BaseFragmentView {

    fun setInfo(data: UserInfoViewModel)

    fun setHead(data: UserHeadViewModel)

    fun setFavorites(isMe: Boolean, it: UserContentViewModel)

    fun setFriends(isMe: Boolean, it: UserContentViewModel)

    fun setClubs(isMe: Boolean, it: UserContentViewModel)

    fun setAnimeRate(data: UserRateViewModel)

    fun setMangaRate(data: UserRateViewModel)

    fun addSettings()

    fun showAuthView(show: Boolean)
}
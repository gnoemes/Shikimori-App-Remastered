package com.gnoemes.shikimori.presentation.presenter.user.converter

import com.gnoemes.shikimori.entity.club.domain.Club
import com.gnoemes.shikimori.entity.user.domain.FavoriteList
import com.gnoemes.shikimori.entity.user.domain.Status
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.domain.UserDetails
import com.gnoemes.shikimori.entity.user.presentation.UserContentViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserHeadViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserInfoViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserRateViewModel

interface UserDetailsViewModelConverter  {

    fun convertHead(t : UserDetails) : UserHeadViewModel

    fun convertInfo(t : UserDetails) : UserInfoViewModel

    fun convertFriends(t : List<UserBrief>) : UserContentViewModel

    fun convertFavorites(t : FavoriteList) : UserContentViewModel

    fun convertClubs(t : List<Club>) : UserContentViewModel

    fun convertAnimeRate(statuses: List<Status>?): UserRateViewModel

    fun convertMangaRate(statuses: List<Status>?): UserRateViewModel

}
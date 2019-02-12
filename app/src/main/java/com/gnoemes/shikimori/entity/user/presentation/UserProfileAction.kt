package com.gnoemes.shikimori.entity.user.presentation

import com.gnoemes.shikimori.entity.rates.domain.RateStatus

sealed class UserProfileAction {
    data class More(val type: UserContentType) : UserProfileAction()
    data class ChangeFriendshipStatus(val newStatus : Boolean) : UserProfileAction()
    data class ChangeIgnoreStatus(val newStatus: Boolean) : UserProfileAction()
    data class RateClicked(val isAnime : Boolean, val status : RateStatus) : UserProfileAction()
    object About : UserProfileAction()
    object History : UserProfileAction()
    object Message : UserProfileAction()
    object Bans : UserProfileAction()
}
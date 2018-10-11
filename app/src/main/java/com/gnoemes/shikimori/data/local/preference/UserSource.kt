package com.gnoemes.shikimori.data.local.preference

import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.domain.UserStatus

interface UserSource {
    fun getUser(): UserBrief?

    fun setUser(user: UserBrief)

    fun clearUser()

    fun getUserStatus(): UserStatus

    fun setUserStatus(status: UserStatus)
}
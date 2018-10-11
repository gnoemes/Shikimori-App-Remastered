package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.UserBriefResponse
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import io.reactivex.functions.Function

interface UserBriefResponseConverter : Function<List<UserBriefResponse?>, List<UserBrief>> {

    fun convertResponse(it: UserBriefResponse?): UserBrief?
}
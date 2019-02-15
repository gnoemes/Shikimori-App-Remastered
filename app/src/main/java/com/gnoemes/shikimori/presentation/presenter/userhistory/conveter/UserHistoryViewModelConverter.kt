package com.gnoemes.shikimori.presentation.presenter.userhistory.conveter

import com.gnoemes.shikimori.entity.user.domain.UserHistory
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryViewModel
import io.reactivex.functions.Function

interface UserHistoryViewModelConverter : Function<List<UserHistory>, List<UserHistoryViewModel>> {

    fun groupItems(items : List<UserHistoryViewModel>) : List<Any>
}
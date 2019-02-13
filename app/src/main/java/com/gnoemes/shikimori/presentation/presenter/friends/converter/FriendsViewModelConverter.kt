package com.gnoemes.shikimori.presentation.presenter.friends.converter

import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.presentation.FriendViewModel
import io.reactivex.functions.Function

interface FriendsViewModelConverter : Function<List<UserBrief>, List<FriendViewModel>>
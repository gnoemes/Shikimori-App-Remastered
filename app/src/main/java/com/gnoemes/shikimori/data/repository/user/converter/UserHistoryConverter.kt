package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.UserHistoryResponse
import com.gnoemes.shikimori.entity.user.domain.UserHistory
import io.reactivex.functions.Function

interface UserHistoryConverter : Function<List<UserHistoryResponse>, List<UserHistory>>
package com.gnoemes.shikimori.data.repository.user.converter

import com.gnoemes.shikimori.entity.user.data.FavoriteListResponse
import com.gnoemes.shikimori.entity.user.domain.FavoriteList
import io.reactivex.functions.Function

interface FavoriteListResponseConverter : Function<FavoriteListResponse, FavoriteList>
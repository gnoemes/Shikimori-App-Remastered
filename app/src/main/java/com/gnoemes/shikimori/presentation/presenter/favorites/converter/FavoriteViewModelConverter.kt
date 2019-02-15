package com.gnoemes.shikimori.presentation.presenter.favorites.converter

import com.gnoemes.shikimori.entity.user.domain.FavoriteList
import com.gnoemes.shikimori.entity.user.presentation.FavoriteViewModel
import io.reactivex.functions.Function

interface FavoriteViewModelConverter : Function<FavoriteList, List<FavoriteViewModel>>
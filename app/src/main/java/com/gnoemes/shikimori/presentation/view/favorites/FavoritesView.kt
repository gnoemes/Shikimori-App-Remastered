package com.gnoemes.shikimori.presentation.view.favorites

import com.gnoemes.shikimori.entity.user.presentation.FavoriteViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface FavoritesView : BaseFragmentView {

    fun showData(items : List<FavoriteViewModel>)

    fun showFavoritesCount(count : Int)
}
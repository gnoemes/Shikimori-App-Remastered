package com.gnoemes.shikimori.entity.user.presentation

import com.gnoemes.shikimori.entity.user.domain.FavoriteType

class FavoriteViewModel(
        val type : FavoriteType,
        val items : List<UserContentItem>
)
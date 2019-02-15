package com.gnoemes.shikimori.presentation.view.favorites.holders

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.domain.FavoriteType
import com.gnoemes.shikimori.entity.user.presentation.FavoriteViewModel
import com.gnoemes.shikimori.presentation.view.favorites.adapter.FavoritesAdapter
import com.gnoemes.shikimori.utils.calculateColumns
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.widgets.GridItemDecorator
import kotlinx.android.synthetic.main.layout_favorite_category.view.*

class FavoriteCategoryViewHolder(
        private val view: View,
        private val adapter: FavoritesAdapter
) {

    init {
        with(view.contentRecyclerView) {
            val spanCount = context.calculateColumns(R.dimen.image_favorite_width_big)
            adapter = this@FavoriteCategoryViewHolder.adapter
            layoutManager = GridLayoutManager(context, spanCount)
            setHasFixedSize(true)
            val spacing = context.dimen(R.dimen.margin_normal).toInt()
            addItemDecoration(GridItemDecorator(spacing, spacing * 2))
        }
    }

    fun bind(item: FavoriteViewModel) {
        if (item.items.isEmpty()) return view.gone()

        with(view) {
            categoryName.setText(getFavoriteCategory(item.type))
            adapter.bindItems(item.items)
        }
    }

    @StringRes
    private fun getFavoriteCategory(type: FavoriteType): Int = when (type) {
        FavoriteType.ANIME -> R.string.common_anime
        FavoriteType.MANGA -> R.string.common_manga
        FavoriteType.SEYU -> R.string.common_seyu
        FavoriteType.PRODUCERS -> R.string.favorites_producers
        FavoriteType.PEOPLE -> R.string.favorites_other_people
        FavoriteType.MANGAKAS -> R.string.favorites_mangakas
        FavoriteType.CHARACTERS -> R.string.common_characters
    }
}
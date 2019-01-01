package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadItem
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.view.common.adapter.GenreAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.layout_details_head.view.*
import kotlinx.android.synthetic.main.layout_details_head_content.view.*
import kotlin.math.roundToInt

class DetailsHeadViewHolder(
        private val view: View,
        private val imageLoader: ImageLoader,
        private val resourceProvider: RatingResourceProvider,
        private val genreAdapter: GenreAdapter,
        private val callback: (DetailsAction) -> Unit
) {

    private val placeholder: DetailsPlaceholderViewHolder by lazy { DetailsPlaceholderViewHolder(view.headContent, view.headPlaceholder as ShimmerFrameLayout) }

    init {
        view.headContent.genresList.apply {
            adapter = genreAdapter
            layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
            setHasFixedSize(true)
        }
    }

    fun bind(item: DetailsHeadItem) {
        placeholder.showContent()

        with(view.headContent) {
            if (!imageView.hasImage()) {
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)
            }

            imageView.onClick { callback.invoke(DetailsAction.Screenshots) }
            studioView.onClick {
                callback.invoke(DetailsAction.StudioClicked(item.studio?.id ?: Constants.NO_ID))
            }
            studioView.visibleIf { item.studio != null }

            ageRatingView.text = item.ageRating
            ageRatingView.visibleIf { item.ageRating != null }

            nameView.text = item.name
            nameSecondView.text = item.nameSecond

            val typeText = context.getString(R.string.details_type).toBold().append(" ").append(item.type)
            val seasonText = context.getString(R.string.details_season).toBold().append(" ").append(item.season)
            val statusText = context.getString(R.string.details_status).toBold().append(" ").append(item.status)
            val studioText = context.getString(R.string.details_studio).toBold().append(" ").append((item.studio?.name
                    ?: "").toLink())

            typeView.text = typeText
            seasonView.text = seasonText
            statusView.text = statusText
            studioView.text = studioText

            ratingView.rating = (item.score / 2).toFloat()
            ratingValueView.text = item.score.toString()
            ratingDescriptionView.text = resourceProvider.getRatingDescription(item.score.roundToInt())

            genreAdapter.bindItems(item.genres)
        }
    }
}
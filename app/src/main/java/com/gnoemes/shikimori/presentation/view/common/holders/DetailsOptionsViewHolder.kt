package com.gnoemes.shikimori.presentation.view.common.holders

import android.view.View
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.SpinnerAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsOptionsItem
import com.gnoemes.shikimori.utils.colorAttr
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.tintCompoundDrawables
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.layout_details_options.view.*

class DetailsOptionsViewHolder(
        private val view: View,
        private val callback: (DetailsAction) -> Unit
) {

    fun bind(item: DetailsOptionsItem) {
        with(view) {
            rateSpinnerView.isAnime = item.isAnime
            rateSpinnerView.setRateStatus(item.rateStatus)
            rateSpinnerView.callback = { spinnerAction, rateStatus ->
                when (spinnerAction) {
                    SpinnerAction.RATE_CHANGE -> callback.invoke(DetailsAction.ChangeRateStatus(rateStatus))
                    SpinnerAction.RATE_EDIT -> callback.invoke(DetailsAction.EditRate())
                }
            }
            rateSpinnerView.visibleIf { !item.isGuest }

            watchOnlineBtn.apply {
                text = item.watchOnlineText
                setOnClickListener { callback.invoke(DetailsAction.WatchOnline()) }
            }

            chronologyView.tintCompoundDrawables(context.colorAttr(R.attr.colorPrimaryDark))
            linksView.tintCompoundDrawables(context.colorAttr(R.attr.colorPrimaryDark))
            discussionView.tintCompoundDrawables(context.colorAttr(R.attr.colorPrimaryDark))

            chronologyView.text = item.chronologyText

            chronologyView.onClick { callback.invoke(DetailsAction.Chronology) }
            linksView.onClick { callback.invoke(DetailsAction.Links) }
            discussionView.onClick { callback.invoke(DetailsAction.Discussion) }
        }
    }
}
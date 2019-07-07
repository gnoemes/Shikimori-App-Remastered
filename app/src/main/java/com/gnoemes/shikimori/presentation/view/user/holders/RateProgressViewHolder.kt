package com.gnoemes.shikimori.presentation.view.user.holders

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.LinearLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.RateProgressStatus
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.drawable
import com.gnoemes.shikimori.utils.tint
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.layout_rate_progress.view.*

class RateProgressViewHolder(
        val view: View,
        val isAnime: Boolean
) {

    init {
        val progressColor: Int = view.context.color(R.color.rate_default_dark)
        val completedColor: Int = view.context.color(R.color.rate_watched_dark)
        val droppedColor: Int = view.context.color(R.color.rate_dropped_dark)

        val plannedIcon = view.context.drawable(R.drawable.ic_planned)?.apply { tint(progressColor) }
        val progressIcon = view.context.drawable(R.drawable.ic_play_rate)?.apply { tint(progressColor) }
        val completedIcon = view.context.drawable(R.drawable.ic_check)?.apply { tint(completedColor) }
        val droppedIcon = view.context.drawable(R.drawable.ic_close)?.apply { tint(droppedColor) }

        with(view) {
            plannedCountView.setCompoundDrawablesWithIntrinsicBounds(null, null, plannedIcon, null)
            watchingCountView.setCompoundDrawablesWithIntrinsicBounds(null, null, progressIcon, null)
            watchedCountView.setCompoundDrawablesWithIntrinsicBounds(null, null, completedIcon, null)
            droppedCountView.setCompoundDrawablesWithIntrinsicBounds(null, null, droppedIcon, null)

            firstSection.background = ColorDrawable(progressColor)
            secondSection.background = ColorDrawable(progressColor)
            thirdSection.background = ColorDrawable(completedColor)
            fourthSection.background = ColorDrawable(droppedColor)
        }
    }

    fun bind(rates: Map<RateProgressStatus, Int>) {
        with(view) {
            val plannedCount = rates.getValue(RateProgressStatus.PLANNED)
            plannedCountView.text = "$plannedCount"

            val watchedCount = rates.getValue(RateProgressStatus.COMPLETED)
            watchedCountView.text = "$watchedCount"

            val watchingCount = rates.getValue(RateProgressStatus.IN_PROGRESS)
            watchingCountView.text = "$watchingCount"

            val droppedCount = rates.getValue(RateProgressStatus.DROPPED)
            droppedCountView.text = "$droppedCount"

            val sum = rates.toList().sumBy { it.second }

            if (sum != 0) {
                val percents = rates.mapValues { it.value / sum.toDouble() }

                firstSection.changeWeight(percents.getValue(RateProgressStatus.PLANNED))
                secondSection.changeWeight(percents.getValue(RateProgressStatus.IN_PROGRESS))
                thirdSection.changeWeight(percents.getValue(RateProgressStatus.COMPLETED))
                fourthSection.changeWeight(percents.getValue(RateProgressStatus.DROPPED))
                progressContainer.invalidate()
            }
        }
    }

    private fun View.changeWeight(newWeight: Double) {
        visibleIf { newWeight != 0.0 }
        (parent as? View)?.post { layoutParams = (layoutParams as LinearLayout.LayoutParams).apply { weight = newWeight.toFloat() } }
    }


}
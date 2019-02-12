package com.gnoemes.shikimori.presentation.view.user.holders

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.LinearLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.RateProgressStatus
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.tint
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.layout_rate_progress.view.*

class RateProgressViewHolder(
        val view: View,
        val isAnime: Boolean
) {

    init {
        val progressColor: Int
        val completedColor: Int
        val droppedColor: Int = view.context.color(R.color.rate_progress_dropped)

        val progressText: Int
        val completedText: Int
        val droppedText = R.string.profile_rate_dropped

        if (isAnime) {
            progressColor = view.context.color(R.color.anime_rate_progress_watching)
            completedColor = view.context.color(R.color.anime_rate_progress_watched)
            progressText = R.string.profile_rate_watching
            completedText = R.string.profile_rate_watched
        } else {
            progressColor = view.context.color(R.color.manga_rate_progress_reading)
            completedColor = view.context.color(R.color.manga_rate_progress_readed)
            progressText = R.string.profile_rate_reading
            completedText = R.string.profile_rate_readed
        }

        with(view) {
            watchingIndicatorView.tint(progressColor)
            watchedIndicatorView.tint(completedColor)
            droppedIndicatorView.tint(droppedColor)

            firstSection.background = ColorDrawable(completedColor)
            secondSection.background = ColorDrawable(progressColor)
            thirdSection.background = ColorDrawable(droppedColor)

            watchingLabel.setText(progressText)
            watchedLabel.setText(completedText)
            droppedLabel.setText(droppedText)
        }
    }

    fun bind(rates: Map<RateProgressStatus, Int>) {
        with(view) {
            val watchedCount = rates.getValue(RateProgressStatus.COMPLETED)
            watchedCountView.text = "$watchedCount"

            val watchingCount = rates.getValue(RateProgressStatus.IN_PROGRESS)
            watchingCountView.text = "$watchingCount"

            val droppedCount = rates.getValue(RateProgressStatus.DROPPED)
            droppedCountView.text = "$droppedCount"

            val sum = rates.toList().sumBy { it.second }

            if (sum != 0) {
                val percents = rates.mapValues { it.value / sum.toDouble() }

                firstSection.changeWeight(percents.getValue(RateProgressStatus.COMPLETED))
                secondSection.changeWeight(percents.getValue(RateProgressStatus.IN_PROGRESS))
                thirdSection.changeWeight(percents.getValue(RateProgressStatus.DROPPED))
                progressContainer.invalidate()
            }
        }
    }

    private fun View.changeWeight(newWeight: Double) {
        visibleIf { newWeight != 0.0 }
        (parent as? View)?.post { layoutParams = (layoutParams as LinearLayout.LayoutParams).apply { weight = newWeight.toFloat() } }
    }


}
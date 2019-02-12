package com.gnoemes.shikimori.presentation.view.user.holders

import android.view.View
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.RateProgressStatus
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.tint
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

            watchingLabel.setText(progressText)
            watchedLabel.setText(completedText)
            droppedLabel.setText(droppedText)
        }
    }

    fun bind(rates: Map<RateProgressStatus, Int>) {
        with(view) {
            val watchedCount = rates.getValue(RateProgressStatus.WATCHED)
            watchedCountView.text = "$watchedCount"

            val watchingCount = rates.getValue(RateProgressStatus.WATCHING)
            watchingCountView.text = "$watchingCount"

            val droppedCount = rates.getValue(RateProgressStatus.DROPPED)
            droppedCountView.text = "$droppedCount"
        }
    }


}
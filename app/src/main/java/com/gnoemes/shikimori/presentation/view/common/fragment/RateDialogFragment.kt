package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.domain.SpinnerAction
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.view.base.fragment.MvpDialogFragment
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.dialog_rate.view.*
import kotlin.math.roundToInt

class RateDialogFragment : MvpDialogFragment() {

    private var callback: RateDialogCallback? = null
    private var isAnime: Boolean = true
    private lateinit var customView: View
    private var rate: UserRate? = null

    companion object {
        fun newInstance(isAnime: Boolean = true, rate: UserRate?) = RateDialogFragment()
                .withArgs {
                    putBoolean(ARGUMENT_IS_ANIME, isAnime)
                    putParcelable(AppExtras.ARGUMENT_RATE, rate)
                }

        private const val ARGUMENT_IS_ANIME = "ARGUMENT_IS_ANIME"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments.ifNotNull {
            rate = it.getParcelable(AppExtras.ARGUMENT_RATE)
        }

        customView = View.inflate(context, R.layout.dialog_rate, null)
        callback = parentFragment as? RateDialogCallback
        initView()

        return MaterialDialog(context!!).show {
            customView(view = customView, scrollable = true)
            positiveButton(res = R.string.common_save) { callback?.onUpdateRate(createRate()) }
            negativeButton(res = R.string.common_cancel)
            rate.ifNotNull { rate ->
                neutralButton(res = R.string.common_delete) {
                    callback?.onDeleteRate(rate.id ?: Constants.NO_ID)
                }
            }
        }
    }

    private fun createRate(): UserRate =
            UserRate(
                    id = rate?.id ?: Constants.NO_ID,
                    score = Math.round(customView.ratingBar.rating * 2).toDouble(),
                    status = rate?.status,
                    episodes = if (isAnime) customView.episodesView?.text?.toString()?.toIntOrNull() else null,
                    chapters = if (isAnime) null else customView.episodesView?.text?.toString()?.toIntOrNull(),
                    text = customView.commentView.text?.toString()
            )

    private fun initView() {
        with(customView) {
            with(rateSpinnerView) {
                hasEdit = false
                setRateStatus(rate?.status)
                callback = { action, status -> if (action == SpinnerAction.RATE_CHANGE) rate = rate?.copy(status = status) }
            }

            val rating = rate?.score?.roundToInt() ?: 0
            ratingBar.rating = rating.div(2).toFloat()
            ratingBar.setOnRatingBarChangeListener { _, fl, _ ->
                val newRating = (fl * 2).roundToInt()
                countRating(newRating)
            }
            countRating(rating)

            commentView.setText(rate?.text)

            if (isAnime) {
                episodesLabelView.text = context.getString(R.string.common_episodes)
                episodesView.setText(rate?.episodes?.toString() ?: "")
            } else {
                episodesLabelView.text = context.getString(R.string.common_chapters)
                episodesView.setText(rate?.chapters?.toString() ?: "")
            }
        }
    }

    private fun countRating(rating: Int) {
        customView.ratingValueView.text = rating.toString()
        customView.ratingDescriptionView.text = getRatingDescription(rating)
    }

    private fun getRatingDescription(rating: Int): String {
        return when (rating) {
            1 -> context!!.getString(R.string.rating_bad_ass)
            2 -> context!!.getString(R.string.rating_awful)
            3 -> context!!.getString(R.string.rating_very_bad)
            4 -> context!!.getString(R.string.rating_bad)
            5 -> context!!.getString(R.string.rating_not_bad)
            6 -> context!!.getString(R.string.rating_normal)
            7 -> context!!.getString(R.string.rating_good)
            8 -> context!!.getString(R.string.rating_fine)
            9 -> context!!.getString(R.string.rating_nuts)
            10 -> context!!.getString(R.string.rating_perfect)
            else -> context!!.getString(R.string.rating_empty)
        }
    }

    interface RateDialogCallback {
        fun onUpdateRate(rate: UserRate) = Unit
        fun onDeleteRate(id: Long) = Unit
    }

}
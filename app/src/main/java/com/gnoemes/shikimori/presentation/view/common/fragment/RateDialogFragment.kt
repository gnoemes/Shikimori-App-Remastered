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
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProviderImpl
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

    private val ratingResourceProvider: RatingResourceProvider by lazy { RatingResourceProviderImpl(context!!) }

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
            isAnime = it.getBoolean(ARGUMENT_IS_ANIME)
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
                isAnime = this@RateDialogFragment.isAnime
                hasEdit = false
                setRateStatus(rate?.status)
                callback = { action, status -> if (action == SpinnerAction.RATE_CHANGE) rate = rate?.copy(status = status) }
            }

            val rating = rate?.score?.roundToInt() ?: 0
            ratingBar.rating = rating.div(2f)
            ratingBar.setOnRatingBarChangeListener { _, fl, _ ->
                val newRating = (fl * 2).roundToInt()
                countRating(newRating)
            }
            countRating(rating)

            ratingGroup.setOnClickListener { view ->
                val currentRating = ratingValueView.text.toString().toIntOrNull()
                currentRating?.let {
                    val newRating = when (it) {
                        10 -> 0
                        else -> it + 1
                    }
                    countRating(newRating)
                }
            }

            episodeIncrementView.setOnClickListener {
                val newValue = episodesView.text?.toString()?.toIntOrNull()?.plus(1) ?: 0
                episodesView.setText(newValue.toString())
            }

            episodeDecrementView.setOnClickListener {
                var newValue = episodesView.text?.toString()?.toIntOrNull()?.minus(1) ?: 0
                if (newValue < 0) newValue = 0
                episodesView.setText(newValue.toString())
            }

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
        customView.ratingDescriptionView.text = ratingResourceProvider.getRatingDescription(rating)
        customView.ratingBar.rating = rating.div(2f)
    }

    interface RateDialogCallback {
        fun onUpdateRate(rate: UserRate) = Unit
        fun onDeleteRate(id: Long) = Unit
    }

}
package com.gnoemes.shikimori.presentation.view.common.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProviderImpl
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_edit_rate.*
import kotlinx.android.synthetic.main.layout_edit_rate_content.*
import kotlinx.android.synthetic.main.layout_edit_rate_progress.*
import kotlinx.android.synthetic.main.layout_edit_rate_status.*
import kotlin.math.roundToInt

class EditRateFragment : BaseBottomSheetDialogFragment() {

    private var callback: RateDialogCallback? = null
    private var isAnime: Boolean = true
    private var rate: UserRate? = null

    private val ratingResourceProvider: RatingResourceProvider by lazy { RatingResourceProviderImpl(context!!) }

    private val chips by lazy {
        mutableListOf(
                ChipRate(R.id.progressBtn, RateStatus.WATCHING, RateStatus.WATCHING == rate?.status),
                ChipRate(R.id.plannedBtn, RateStatus.PLANNED, RateStatus.PLANNED == rate?.status),
                ChipRate(R.id.reProgressBtn, RateStatus.REWATCHING, RateStatus.REWATCHING == rate?.status),
                ChipRate(R.id.completedBtn, RateStatus.COMPLETED, RateStatus.COMPLETED == rate?.status),
                ChipRate(R.id.onHoldBtn, RateStatus.ON_HOLD, RateStatus.ON_HOLD == rate?.status),
                ChipRate(R.id.droppedBtn, RateStatus.DROPPED, RateStatus.DROPPED == rate?.status)
        )
    }

    companion object {
        private const val IS_ANIME_KEY = "IS_ANIME_KEY"
        private const val RATE_KEY = "RATE_KEY"
        private const val TITLE = "TITLE"
        fun newInstance(title: String, isAnime: Boolean = true, rate: UserRate?) = EditRateFragment()
                .withArgs {
                    putString(TITLE, title)
                    putBoolean(IS_ANIME_KEY, isAnime)
                    putParcelable(RATE_KEY, rate)
                }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = context.dp(210)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getDialogLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            isAnime = getBoolean(IS_ANIME_KEY, true)
            rate = savedInstanceState?.getParcelable(RATE_KEY) ?: getParcelable(RATE_KEY)
        }
        callback = parentFragment as? RateDialogCallback

        toolbar.title = arguments?.getString(TITLE)

        //need rate from arguments
        deleteBtn.visibleIf { arguments?.getParcelable<UserRate>(RATE_KEY)?.status != null }
        deleteBtn.onClick { callback?.onDeleteRate(rate?.id ?: Constants.NO_ID); dismiss() }

        acceptBtn.onClick {
            callback?.onUpdateRate(createRate())
            dismiss()
        }

        val rating = rate?.score?.roundToInt() ?: 0
        ratingBar.rating = rating.div(2f)
        ratingBar.setEmptyDrawable(context!!.drawable(R.drawable.ic_big_star_empty)?.apply { tint(context!!.colorAttr(R.attr.colorOnPrimarySecondary)) })
        ratingBar.setFilledDrawable(context!!.drawable(R.drawable.ic_big_star_filled)?.apply { tint(context!!.colorAttr(R.attr.colorSecondary)) })
        ratingBar.setOnRatingChangeListener { _, fl ->
            val newRating = (fl * 2).roundToInt()
            countRating(newRating)
        }
        countRating(rating)

        ratingGroup.setOnClickListener {
            val currentRating = ratingValueView.text.toString().toIntOrNull()
            currentRating?.let {
                val newRating = when (it) {
                    10 -> 0
                    else -> it + 1
                }
                countRating(newRating)
            }
        }

        progressIncrementView.setOnClickListener {
            val newValue = progressView.text?.toString()?.toIntOrNull()?.plus(1) ?: 0
            progressView.setText(newValue.toString())
        }

        progressDecrementView.setOnClickListener {
            var newValue = progressView.text?.toString()?.toIntOrNull()?.minus(1) ?: 0
            if (newValue < 0) newValue = 0
            progressView.setText(newValue.toString())
        }

        commentView.setText(rate?.text)

        if (isAnime) {
            progressLabelView.text = context!!.getString(R.string.profile_rate_watched)
            progressView.setText(rate?.episodes?.toString() ?: "0")

            rewatchesLabel.text = context!!.getString(R.string.profile_rate_rewatched)
        } else {
            progressLabelView.text = context!!.getString(R.string.profile_rate_readed)
            progressView.setText(rate?.chapters?.toString() ?: "0")

            rewatchesLabel.text = context!!.getString(R.string.profile_rate_reread)
        }

        rewatchesView.setText(rate?.rewatches?.toString() ?: "0")

        progressLabel.setText(if (isAnime) R.string.rate_watching else R.string.rate_reading)
        reProgressLabel.setText(if (isAnime) R.string.rate_rewatch_short else R.string.rate_rereading)
        completedLabel.setText(if (isAnime) R.string.rate_completed else R.string.rate_readed)

        progressBtn.onClick { onStatusChanged(it.id) }
        plannedBtn.onClick { onStatusChanged(it.id) }
        reProgressBtn.onClick { onStatusChanged(it.id) }
        completedBtn.onClick { onStatusChanged(it.id) }
        onHoldBtn.onClick { onStatusChanged(it.id) }
        droppedBtn.onClick { onStatusChanged(it.id) }

        val checkedItem = chips.firstOrNull { it.isSelected }
        if (checkedItem != null) (rateInclude as? ConstraintLayout)?.findViewById<MaterialButton>(checkedItem.id)?.isSelected = true

        if (rate == null) rate = createRate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(RATE_KEY, rate)
    }

    private fun onStatusChanged(id: Int) {
        (rateInclude as? ConstraintLayout)?.forEach { btn ->
            if (btn is MaterialButton) {
                val item = chips.find { it.id == id }
                btn.isSelected = false
                if (btn.id == id && item != null) {
                    btn.isSelected = true
                    rate = rate?.copy(status = item.status)
                }
            }
        }
    }

    private fun createRate(): UserRate =
            UserRate(
                    id = rate?.id ?: Constants.NO_ID,
                    score = Math.round(ratingBar.rating * 2).toDouble(),
                    status = rate?.status,
                    rewatches = rewatchesView?.text?.toString()?.toIntOrNull(),
                    episodes = if (isAnime) progressView.text?.toString()?.toIntOrNull() else null,
                    chapters = if (isAnime) null else progressView.text?.toString()?.toIntOrNull(),
                    text = commentView.text?.toString()
            )

    private fun countRating(rating: Int) {
        ratingValueView.text = rating.toString()
        ratingDescriptionView.text = ratingResourceProvider.getRatingDescription(rating)
        ratingBar.rating = rating.div(2f)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.fragment_edit_rate

    override val windowBackground: Int by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) R.drawable.bg_rate_dialog_window
        else context!!.attr(R.attr.editRateBackground).resourceId
    }


    interface RateDialogCallback {
        fun onUpdateRate(rate: UserRate) = Unit
        fun onDeleteRate(id: Long) = Unit
    }

    private data class ChipRate(
            val id: Int,
            val status: RateStatus,
            val isSelected: Boolean
    )
}
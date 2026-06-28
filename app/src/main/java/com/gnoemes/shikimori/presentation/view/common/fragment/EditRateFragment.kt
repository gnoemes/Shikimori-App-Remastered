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
import com.gnoemes.shikimori.databinding.FragmentEditRateBinding
import com.gnoemes.shikimori.databinding.LayoutEditRateContentBinding
import com.gnoemes.shikimori.databinding.LayoutEditRateProgressBinding
import com.gnoemes.shikimori.databinding.LayoutEditRateStatusBinding
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.domain.UserRate
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProvider
import com.gnoemes.shikimori.presentation.presenter.common.provider.RatingResourceProviderImpl
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.*
import com.google.android.material.button.MaterialButton
import kotlin.math.roundToInt

class EditRateFragment : BaseBottomSheetDialogFragment() {

    private var callback: RateDialogCallback? = null
    private var isAnime: Boolean = true
    private var rate: UserRate? = null

    private var binding: FragmentEditRateBinding? = null
    private var contentBinding: LayoutEditRateContentBinding? = null
    private var progressBinding: LayoutEditRateProgressBinding? = null
    private var statusBinding: LayoutEditRateStatusBinding? = null

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
        binding = FragmentEditRateBinding.inflate(inflater, container, false)
        val b = binding ?: return null
        contentBinding = b.includedLayoutEditRateContent
        val cb = contentBinding ?: return null
        progressBinding = LayoutEditRateProgressBinding.bind(cb.progressInclude.root)
        statusBinding = LayoutEditRateStatusBinding.bind(cb.rateInclude.root)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            isAnime = getBoolean(IS_ANIME_KEY, true)
            @Suppress("DEPRECATION")
            rate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState?.getParcelable(RATE_KEY, UserRate::class.java) ?: getParcelable(RATE_KEY, UserRate::class.java)
            } else {
                savedInstanceState?.getParcelable(RATE_KEY) ?: getParcelable(RATE_KEY)
            }
        }
        callback = parentFragment as? RateDialogCallback

        binding?.toolbar?.let { it.title = arguments?.getString(TITLE) }

        //need rate from arguments
        binding?.deleteBtn?.let {
            it.visibleIf { arguments?.getParcelable<UserRate>(RATE_KEY)?.status != null }
            it.onClick { callback?.onDeleteRate(rate?.id ?: Constants.NO_ID); dismiss() }
        }

        binding?.acceptBtn?.onClick {
            createRate()?.let { callback?.onUpdateRate(it) }
            dismiss()
        }

        val rating = rate?.score?.roundToInt() ?: 0
        contentBinding?.ratingBar?.rating = rating.div(2f)
        val emptyDrawable = context!!.drawable(R.drawable.ic_big_star_empty) ?: return@onViewCreated
        emptyDrawable.setTintList(context!!.colorStateList(context!!.attr(R.attr.colorOnPrimarySecondary).resourceId))
        contentBinding?.ratingBar?.let { ratingBar ->
            ratingBar.setEmptyDrawable(emptyDrawable)
            val filledDrawable = context!!.drawable(R.drawable.ic_big_star_filled) ?: return@let
            filledDrawable.setTintList(context!!.colorStateList(context!!.attr(R.attr.colorSecondary).resourceId))
            ratingBar.setFilledDrawable(filledDrawable)
            ratingBar.setOnRatingChangeListener { _, fl, _ ->
                val newRating = (fl * 2).roundToInt()
                countRating(newRating)
            }
        }
        countRating(rating)

        contentBinding?.ratingGroup?.setOnClickListener {
            val currentRating = contentBinding?.ratingValueView?.text?.toString()?.toIntOrNull()
            currentRating?.let {
                val newRating = when (it) {
                    10 -> 0
                    else -> it + 1
                }
                countRating(newRating)
            }
        }

        val pb = progressBinding
        val sb = statusBinding
        val cb = contentBinding

        pb?.progressIncrementView?.setOnClickListener {
            val newValue = pb?.progressView?.text?.toString()?.toIntOrNull()?.plus(1) ?: 0
            pb?.progressView?.setText(newValue.toString())
        }

        pb?.progressDecrementView?.setOnClickListener {
            var newValue = pb?.progressView?.text?.toString()?.toIntOrNull()?.minus(1) ?: 0
            if (newValue < 0) newValue = 0
            pb?.progressView?.setText(newValue.toString())
        }

        cb?.commentView?.setText(rate?.text)

        if (isAnime) {
            pb?.progressLabelView?.text = context?.getString(R.string.profile_rate_watched)
            pb?.progressView?.setText(rate?.episodes?.toString() ?: "0")

            pb?.rewatchesLabel?.text = context?.getString(R.string.profile_rate_rewatched)
        } else {
            pb?.progressLabelView?.text = context?.getString(R.string.profile_rate_readed)
            pb?.progressView?.setText(rate?.chapters?.toString() ?: "0")

            pb?.rewatchesLabel?.text = context?.getString(R.string.profile_rate_reread)
        }

        pb?.rewatchesView?.setText(rate?.rewatches?.toString() ?: "0")

        sb?.progressLabel?.setText(if (isAnime) R.string.rate_watching else R.string.rate_reading)
        sb?.reProgressLabel?.setText(if (isAnime) R.string.rate_rewatch_short else R.string.rate_rereading)
        sb?.completedLabel?.setText(if (isAnime) R.string.rate_completed else R.string.rate_readed)

        sb?.progressBtn?.onClick { onStatusChanged(it.id) }
        sb?.plannedBtn?.onClick { onStatusChanged(it.id) }
        sb?.reProgressBtn?.onClick { onStatusChanged(it.id) }
        sb?.completedBtn?.onClick { onStatusChanged(it.id) }
        sb?.onHoldBtn?.onClick { onStatusChanged(it.id) }
        sb?.droppedBtn?.onClick { onStatusChanged(it.id) }

        val checkedItem = chips.firstOrNull { it.isSelected }
        if (checkedItem != null) sb?.root?.findViewById<MaterialButton>(checkedItem.id)?.isSelected = true

        if (rate == null) rate = createRate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(RATE_KEY, rate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        contentBinding = null
        progressBinding = null
        statusBinding = null
    }

    private fun onStatusChanged(id: Int) {
        statusBinding?.root?.forEach { btn ->
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

    private fun createRate(): UserRate? {
        val cb = contentBinding ?: return null
        val pb = progressBinding ?: return null
        return UserRate(
                id = rate?.id ?: Constants.NO_ID,
                score = Math.round(cb.ratingBar.rating * 2).toDouble(),
                status = rate?.status,
                rewatches = pb.rewatchesView?.text?.toString()?.toIntOrNull(),
                episodes = if (isAnime) pb.progressView.text?.toString()?.toIntOrNull() else null,
                chapters = if (isAnime) null else pb.progressView.text?.toString()?.toIntOrNull(),
                text = cb.commentView.text?.toString()
        )
    }

    private fun countRating(rating: Int) {
        val cb = contentBinding ?: return
        cb.ratingValueView.text = rating.toString()
        cb.ratingDescriptionView.text = ratingResourceProvider.getRatingDescription(rating)
        cb.ratingBar.rating = rating.div(2f)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.fragment_edit_rate

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
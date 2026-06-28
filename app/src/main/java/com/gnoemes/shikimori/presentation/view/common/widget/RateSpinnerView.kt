package com.gnoemes.shikimori.presentation.view.common.widget

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ViewRateSpinnerBinding
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.SpinnerAction
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.widget.BaseView
import com.gnoemes.shikimori.presentation.view.common.widget.spinner.MaterialSpinnerAdapter
import com.gnoemes.shikimori.utils.*

class RateSpinnerView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleInt: Int = 0
) : BaseView(context, attrs, defStyleInt) {

    companion object {
        const val SMALL_SIZE = 0
        const val NORMAL_SIZE = 1
    }

    var isAnime: Boolean = true
    var hasEdit: Boolean = true
    var hasIcon: Boolean = true
    var itemSize: Int = NORMAL_SIZE

    lateinit var callback: (SpinnerAction, RateStatus?) -> Unit
    private var status: RateStatus? = null
    private lateinit var items: MutableList<ViewModel>

    private var colorRes: Int = R.color.rate_default
    private var colorDarkRes: Int = R.color.rate_default_dark
    private var spinnerItemLayout: Int = 0

    private lateinit var binding: ViewRateSpinnerBinding

    override fun getLayout(): Int = R.layout.view_rate_spinner

    override fun init(context: Context, attrs: AttributeSet?) {
        binding = ViewRateSpinnerBinding.inflate(android.view.LayoutInflater.from(context), this, true)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.RateSpinnerView)
        isAnime = ta.getBoolean(R.styleable.RateSpinnerView_anime, true)
        hasEdit = ta.getBoolean(R.styleable.RateSpinnerView_showEdit, true)
        hasIcon = ta.getBoolean(R.styleable.RateSpinnerView_showIcon, true)
        itemSize = ta.getInt(R.styleable.RateSpinnerView_itemSize, NORMAL_SIZE)
        spinnerItemLayout = if (itemSize == SMALL_SIZE) R.layout.item_rate_spinner_small else R.layout.item_rate_spinner
        ta.recycle()

        items = mutableListOf(
                ViewModel(R.drawable.ic_play_rate, R.attr.colorRateDefault, R.color.rate_default_dark, RateStatus.WATCHING, 0),
                ViewModel(R.drawable.ic_planned, R.attr.colorRateDefault, R.color.rate_default_dark, RateStatus.PLANNED, 1),
                ViewModel(R.drawable.ic_replay, R.attr.colorRateDefault, R.color.rate_default_dark, RateStatus.REWATCHING, 2),
                ViewModel(R.drawable.ic_check, R.attr.colorRateWatched, R.color.rate_watched_dark, RateStatus.COMPLETED, 3),
                ViewModel(R.drawable.ic_pause_rate, R.attr.colorRateOnHold, R.color.rate_on_hold_dark, RateStatus.ON_HOLD, 4),
                ViewModel(R.drawable.ic_close, R.attr.colorRateDropped, R.color.rate_dropped_dark, RateStatus.DROPPED, 5)
        )

        val defaultPadding = context.dp(16)

        when (itemSize) {
            SMALL_SIZE -> binding.spinnerView.apply { textSize = 12f; setPadding(defaultPadding, context.dp(8), defaultPadding, context.dp(8)); adapterTextSize = 14f }
            else -> binding.spinnerView.apply { textSize = 16f; setPadding(defaultPadding, context.dp(12), defaultPadding, context.dp(12)); adapterTextSize = 14f }
        }

        binding.rateImage.visibleIf { hasIcon }

        if (!isInEditMode) {
            updateColor()
        } else {
            binding.editBtn.visibleIf { hasEdit }
        }

        binding.editBtn.setOnClickListener {
            if (::callback.isInitialized) {
                callback.invoke(SpinnerAction.RATE_EDIT, status)
            }
        }
    }

    private fun initAdapter() {
        val arrayRes =
                if (isAnime) when (status) {
                    null -> R.array.anime_rate_stasuses_empty
                    else -> R.array.anime_rate_stasuses
                } else when (status) {
                    null -> R.array.manga_rate_stasuses_empty
                    else -> R.array.manga_rate_stasuses
                }

        val selection = items.firstOrNull { it.status == status }?.pos ?: 0

        binding.spinnerView.apply {
            adapterTextColor = context.colorAttr(R.attr.colorOnSurface)
            setAdapter(MaterialSpinnerAdapter<String>(context, context.resources.getStringArray(arrayRes).toMutableList()))
            selectedIndex = selection

            popupWindow.setBackgroundDrawable(context.drawable(R.drawable.background_player_spinner)?.apply {
                tint(context.colorAttr(R.attr.colorDialogSurface))
            })
            setOnItemSelectedListener { _, pos, _, item ->
                val position = if (arrayRes == R.array.anime_rate_stasuses_empty || arrayRes == R.array.manga_rate_stasuses_empty) pos - 1 else pos
                status = items.firstOrNull { it.pos == position }?.status

                updateColor()

                if (::callback.isInitialized && status != null) {
                    callback.invoke(SpinnerAction.RATE_CHANGE, status!!)
                }
            }
        }
    }

    fun setRateStatus(status: RateStatus?) {
        this.status = status
        updateColor()
    }

    private fun updateColor() {
        val item = items.firstOrNull { it.status == status }
            colorRes =
                    try {
                        context.attr(item?.colorRes!!).resourceId
                    } catch (e : Exception) {
                        R.color.rate_default_transparent
                    }

            colorDarkRes = item?.colorDarkRes ?: R.color.rate_default_dark

            initAdapter()

            binding.container.setCardBackgroundColor(context.color(colorRes))
            binding.container.strokeColor = context.color(colorDarkRes)
            binding.rateImage.setImageResource(item?.iconRes ?: R.drawable.ic_plus)
            binding.rateImage.tintWithRes(colorDarkRes)
            binding.editBtn.tintWithRes(colorDarkRes)

            binding.spinnerView.setTextColor(context.color(colorDarkRes))
            binding.spinnerView.setArrowColor(context.color(colorDarkRes))

            binding.editBtn.visibleIf { hasEdit }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return Bundle()
                .apply {
                    putParcelable("superState", super.onSaveInstanceState())
                    putSerializable(AppExtras.ARGUMENT_RATE_STATUS, status)
                }
    }

    @Suppress("DEPRECATION")
    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is Bundle -> {
                this.status = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    state.getSerializable(AppExtras.ARGUMENT_RATE_STATUS, RateStatus::class.java) as? RateStatus
                } else {
                    state.getSerializable(AppExtras.ARGUMENT_RATE_STATUS) as? RateStatus
                }
                super.onRestoreInstanceState(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        state.getParcelable("superState", Parcelable::class.java)
                    } else {
                        state.getParcelable("superState")
                    }
                )
            }
            else -> super.onRestoreInstanceState(state)
        }
    }

    val primaryColor: Int
        get() = colorRes

    val onPrimaryColor: Int
        get() = colorDarkRes

    private data class ViewModel(
            val iconRes: Int,
            val colorRes: Int,
            val colorDarkRes: Int,
            val status: RateStatus,
            val pos: Int
    )

}

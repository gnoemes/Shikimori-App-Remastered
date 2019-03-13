package com.gnoemes.shikimori.presentation.view.common.widget

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.AdapterView
import android.widget.TextView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.SpinnerAction
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.widget.BaseView
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.tint
import com.gnoemes.shikimori.utils.tintWithRes
import com.gnoemes.shikimori.utils.visibleIf
import com.gnoemes.shikimori.utils.widgets.SpinnerArrayAdapter
import kotlinx.android.synthetic.main.view_rates_chooser.view.*

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
    var itemSize : Int = NORMAL_SIZE

    lateinit var callback: (SpinnerAction, RateStatus) -> Unit
    private var status: RateStatus? = null
    private lateinit var items: MutableList<ViewModel>

    private var colorRes: Int = R.color.rate_default
    private var colorDarkRes: Int = R.color.rate_default_dark
    private var spinnerItemLayout: Int = 0

    override fun getLayout(): Int = R.layout.view_rates_chooser

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RateSpinnerView)
        isAnime = ta.getBoolean(R.styleable.RateSpinnerView_anime, true)
        hasEdit = ta.getBoolean(R.styleable.RateSpinnerView_showEdit, true)
        hasIcon = ta.getBoolean(R.styleable.RateSpinnerView_showIcon, true)
        itemSize = ta.getInt(R.styleable.RateSpinnerView_itemSize, NORMAL_SIZE)
        spinnerItemLayout = if (itemSize == SMALL_SIZE) R.layout.item_rate_spinner_small else R.layout.item_rate_spinner
        ta.recycle()

        items = mutableListOf(
                ViewModel(R.drawable.ic_play_rate, R.color.rate_default, R.color.rate_default_dark, RateStatus.WATCHING, 0),
                ViewModel(R.drawable.ic_plus, R.color.rate_default, R.color.rate_default_dark, RateStatus.PLANNED, 1),
                ViewModel(R.drawable.ic_replay, R.color.rate_default, R.color.rate_default_dark, RateStatus.REWATCHING, 2),
                ViewModel(R.drawable.ic_check, R.color.rate_watched, R.color.rate_watched_dark, RateStatus.COMPLETED, 3),
                ViewModel(R.drawable.ic_pause_rate, R.color.rate_on_hold, R.color.rate_on_hold_dark, RateStatus.ON_HOLD, 4),
                ViewModel(R.drawable.ic_close, R.color.rate_dropped, R.color.rate_dropped_dark, RateStatus.DROPPED, 5)
        )

        rateImage.visibleIf { hasIcon }

        if (!isInEditMode) {
            updateColor()
        } else {
            editBtn.visibleIf { hasEdit }
        }

        editBtn.setOnClickListener {
            if (::callback.isInitialized && status != null) {
                callback.invoke(SpinnerAction.RATE_EDIT, status!!)

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

        spinnerView.adapter = SpinnerArrayAdapter(colorRes, colorDarkRes, context, spinnerItemLayout, context.resources.getStringArray(arrayRes).toMutableList())
        val selection = items.firstOrNull { it.status == status }?.pos ?: 0
        spinnerView.setSelection(selection, false)
        spinnerView.itemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->

            val position = if (arrayRes == R.array.anime_rate_stasuses_empty || arrayRes == R.array.manga_rate_stasuses_empty) pos - 1 else pos
            status = items.firstOrNull { it.pos == position }?.status

            updateColor()

            if (::callback.isInitialized && status != null) {
                callback.invoke(SpinnerAction.RATE_CHANGE, status!!)
            }
        }
    }

    fun setRateStatus(status: RateStatus?) {
        this.status = status
        updateColor()
    }

    private fun updateColor() {
        val item = items.firstOrNull { it.status == status }

        colorRes = item?.colorRes ?: R.color.rate_default
        colorDarkRes = item?.colorDarkRes ?: R.color.rate_default_dark

        initAdapter()

        container.setBackgroundColor(context.color(colorRes))
        indicatorView.setBackgroundColor(context.color(colorDarkRes))
        rateImage.setImageResource(item?.iconRes ?: R.drawable.ic_plus)
        rateImage.tintWithRes(colorDarkRes)
        editBtn.tintWithRes(colorDarkRes)

        (spinnerView.getChildAt(0) as? TextView)?.apply {
            setBackgroundColor(context.color(colorRes))
            setTextColor(context.color(colorDarkRes))
        }

        val background = spinnerView.background
        spinnerView.background = background.apply { tint(context.color(colorDarkRes)); mutate() }
        spinnerView.postInvalidate()

        editBtn.visibleIf { hasEdit && item != null }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return Bundle()
                .apply {
                    putParcelable("superState", super.onSaveInstanceState())
                    putSerializable(AppExtras.ARGUMENT_RATE_STATUS, status)
                }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is Bundle -> {
                this.status = state.getSerializable(AppExtras.ARGUMENT_RATE_STATUS) as? RateStatus
                super.onRestoreInstanceState(state.getParcelable("superState"))
            }
            else -> super.onRestoreInstanceState(state)
        }
    }

    private data class ViewModel(
            val iconRes: Int,
            val colorRes: Int,
            val colorDarkRes: Int,
            val status: RateStatus,
            val pos: Int
    )

}

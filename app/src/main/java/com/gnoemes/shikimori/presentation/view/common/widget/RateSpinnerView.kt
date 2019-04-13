package com.gnoemes.shikimori.presentation.view.common.widget

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.domain.SpinnerAction
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.widget.BaseView
import com.gnoemes.shikimori.presentation.view.common.widget.spinner.MaterialSpinnerAdapter
import com.gnoemes.shikimori.utils.*
import kotlinx.android.synthetic.main.view_rate_spinner.view.*

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

    lateinit var callback: (SpinnerAction, RateStatus) -> Unit
    private var status: RateStatus? = null
    private lateinit var items: MutableList<ViewModel>

    private var colorRes: Int = R.color.rate_default
    private var colorDarkRes: Int = R.color.rate_default_dark
    private var spinnerItemLayout: Int = 0

    private var isDarkTheme = false

    override fun getLayout(): Int = R.layout.view_rate_spinner

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RateSpinnerView)
        isAnime = ta.getBoolean(R.styleable.RateSpinnerView_anime, true)
        hasEdit = ta.getBoolean(R.styleable.RateSpinnerView_showEdit, true)
        hasIcon = ta.getBoolean(R.styleable.RateSpinnerView_showIcon, true)
        itemSize = ta.getInt(R.styleable.RateSpinnerView_itemSize, NORMAL_SIZE)
        isDarkTheme = context.dimenAttr(R.attr.rateStyleStrokeWidth) != 0
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

        val defaultPadding = context.dp(16)

        when (itemSize) {
            SMALL_SIZE -> spinnerView.apply { textSize = 12f; setPadding(defaultPadding, context.dp(8), defaultPadding, context.dp(8)); adapterTextSize = 14f }
            else -> spinnerView.apply { textSize = 16f; setPadding(defaultPadding, context.dp(12), defaultPadding, context.dp(12)); adapterTextSize = 14f }
        }

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

        val selection = items.firstOrNull { it.status == status }?.pos ?: 0

        spinnerView.apply {
            adapterTextColor = if (isDarkTheme) context.colorAttr(R.attr.colorOnSurface) else context.color(colorDarkRes)
            setAdapter(MaterialSpinnerAdapter<String>(context, context.resources.getStringArray(arrayRes).toMutableList()))
            selectedIndex = selection

            popupWindow.setBackgroundDrawable(context.drawable(R.drawable.background_player_spinner)?.apply {
                tint(if (isDarkTheme) context.color(R.color.dark_colorDialogSurface) else context.color(colorRes))
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

        colorRes = item?.colorRes ?: R.color.rate_default
        colorDarkRes = item?.colorDarkRes ?: R.color.rate_default_dark

        initAdapter()

        container.setCardBackgroundColor(if (isDarkTheme) Color.TRANSPARENT else context.color(colorRes))
        container.strokeColor = context.color(colorDarkRes)
        rateImage.setImageResource(item?.iconRes ?: R.drawable.ic_plus)
        rateImage.tintWithRes(colorDarkRes)
        editBtn.tintWithRes(colorDarkRes)

        spinnerView.setTextColor(context.color(colorDarkRes))
        spinnerView.setArrowColor(context.color(colorDarkRes))

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

    val primaryColor: Int
        get() = if (isDarkTheme) android.R.color.transparent else colorRes

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

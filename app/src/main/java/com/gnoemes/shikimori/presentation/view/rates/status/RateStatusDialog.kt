package com.gnoemes.shikimori.presentation.view.rates.status

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.colorStateList
import com.gnoemes.shikimori.utils.dimenAttr
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.dialog_menu.*

class RateStatusDialog : BaseBottomSheetDialogFragment() {

    companion object {
        fun newInstance(id: Long, title: String, currentStatus: RateStatus?, isAnime: Boolean) = RateStatusDialog().withArgs {
            putLong(ID, id)
            putString(TITLE, title)
            putBoolean(IS_ANIME, isAnime)
            putInt(CURRENT_STATUS, currentStatus?.ordinal ?: -1)
        }

        private const val ID = "ID"
        private const val TITLE = "TITLE"
        private const val IS_ANIME = "IS_ANIME"
        private const val CURRENT_STATUS = "CURRENT_STATUS"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = context.dimenAttr(android.R.attr.actionBarSize)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getDialogLayout(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isAnime = arguments?.getBoolean(IS_ANIME) != false

        with(toolbar) {
            title = arguments?.getString(TITLE)
        }

        val index = arguments?.getInt(CURRENT_STATUS) ?: -1

        val currentStatus = RateStatus.values().getOrNull(index)

        val items = context!!.resources.getStringArray(if (isAnime) R.array.anime_rate_stasuses else R.array.manga_rate_stasuses)
                .zip(RateStatus.values())
                .map { Rate(it.second.ordinal, it.second, it.first, currentStatus == it.second, getIcon(it.second), getTintColor(it.second), getSelector(it.second)) }

        navView.apply {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val item = items.firstOrNull { it.isSelected }
                if (item != null) {
                    itemTextColor = context.colorStateList(item.tint)
                    itemIconTintList = context.colorStateList(item.tint)
                    setItemBackgroundResource(item.selector)
                }
            }

            setNavigationItemSelectedListener { menu ->
                val id = arguments?.getLong(ID) ?: Constants.NO_ID
                (parentFragment as? RateStatusCallback)?.onStatusChanged(id, items.first { it.id == menu.itemId }.status)
                dismiss()
                true
            }

            menu.apply {
                items.forEach {
                    add(0, it.id, it.id, it.text)
                    findItem(it.id)?.setIcon(it.icon)
                }
                val checkedId = items.find { it.isSelected }?.id ?: -1
                setGroupCheckable(0, true, true)
                if (checkedId != -1) setCheckedItem(checkedId)
            }
        }
    }

    private fun getIcon(status: RateStatus): Int {
        return when (status) {
            RateStatus.WATCHING -> R.drawable.ic_play_rate
            RateStatus.PLANNED -> R.drawable.ic_planned
            RateStatus.REWATCHING -> R.drawable.ic_replay
            RateStatus.COMPLETED -> R.drawable.ic_check
            RateStatus.ON_HOLD -> R.drawable.ic_pause_rate
            RateStatus.DROPPED -> R.drawable.ic_close
        }
    }

    private fun getTintColor(status: RateStatus): Int {
        return when (status) {
            RateStatus.WATCHING -> R.color.selector_rate_item_menu_text_color_blue
            RateStatus.PLANNED -> R.color.selector_rate_item_menu_text_color_blue
            RateStatus.REWATCHING -> R.color.selector_rate_item_menu_text_color_blue
            RateStatus.COMPLETED -> R.color.selector_rate_item_menu_text_color_green
            RateStatus.ON_HOLD -> R.color.selector_rate_item_menu_text_color_gray
            RateStatus.DROPPED -> R.color.selector_rate_item_menu_text_color_red
        }
    }

    private fun getSelector(status: RateStatus): Int {
        return when (status) {
            RateStatus.WATCHING -> R.drawable.selector_rate_item_menu_background_default_second
            RateStatus.PLANNED -> R.drawable.selector_rate_item_menu_background_default_second
            RateStatus.REWATCHING -> R.drawable.selector_rate_item_menu_background_default_second
            RateStatus.COMPLETED -> R.drawable.selector_rate_item_menu_background_watched_second
            RateStatus.ON_HOLD -> R.drawable.selector_rate_item_menu_background_on_hold_second
            RateStatus.DROPPED -> R.drawable.selector_rate_item_menu_background_dropped_second
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.dialog_menu

    private data class Rate(
            val id: Int,
            val status: RateStatus,
            val text: String,
            val isSelected: Boolean,
            @DrawableRes val icon: Int,
            @ColorRes val tint: Int,
            @DrawableRes val selector: Int
    )

    interface RateStatusCallback {
        fun onStatusChanged(id: Long, newStatus: RateStatus)
    }
}
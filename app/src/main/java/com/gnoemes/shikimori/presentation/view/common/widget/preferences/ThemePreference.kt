package com.gnoemes.shikimori.presentation.view.common.widget.preferences

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.view_theme_preference.view.*
import org.joda.time.LocalTime

class ThemePreference @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleInt: Int = 0
) : Preference(context, attrs, defStyleInt) {

    companion object {
        private const val TIME_FORMAT = "HH:mm"
    }

    private var theme: Int = R.style.ShikimoriAppTheme_Default
    private var nightTheme: Int = Constants.NO_ID.toInt()
    private var nightStartMills: Int = 0
    private var nightEndMills: Int = 0

    var nightTimeStartClickListener: View.OnClickListener? = null
    var nightTimeEndClickListener: View.OnClickListener? = null

    init {
        layoutResource = R.layout.view_theme_preference
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        if (defaultValue is Int) setTheme(defaultValue)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false
        with(holder.itemView) {
            defaultThemeView.onClick { onDefaultClicked() }
            darkThemeView.onClick { onDarkClicked() }
            amoledThemeView.onClick { onAmoledClicked() }

            darkThemeView.setOnLongClickListener { onDarkLongClick() }
            amoledThemeView.setOnLongClickListener { onAmoledLongClick() }

            nightTimeStartContainer.onClick { nightTimeStartClickListener?.onClick(it) }
            nightTimeEndContainer.onClick { nightTimeEndClickListener?.onClick(it) }

            when (theme) {
                R.style.ShikimoriAppTheme_Default -> defaultThemeView.isChecked = true
                R.style.ShikimoriAppTheme_Dark -> darkThemeView.isChecked = true
                R.style.ShikimoriAppTheme_Amoled -> amoledThemeView.isChecked = true
            }

            when (nightTheme) {
                R.style.ShikimoriAppTheme_Dark -> darkThemeView.isNight = true
                R.style.ShikimoriAppTheme_Amoled -> amoledThemeView.isNight = true
                else -> Unit
            }
            updateNightModeSchedule(this)

            val nightModeStartText = LocalTime(nightStartMills.toLong()).toString(TIME_FORMAT)
            nightTimeStartSummaryView.text = nightModeStartText
            val nightModeEndText = LocalTime(nightEndMills.toLong()).toString(TIME_FORMAT)
            nightTimeEndSummaryView.text = nightModeEndText
        }
    }

    private fun onThemeChange(parentView: View, view: ThemeView, @StyleRes style: Int) {
        updateLightMode(parentView)
        view.isChecked = true
        theme = style
        clearNightThemeIfEmpty(parentView)
        updateNightModeSchedule(parentView)
    }

    private fun onNightThemeChange(parentView: View, view: ThemeView, @StyleRes style: Int): Boolean {
        val newState = !view.isNight
        updateNightMode(parentView)
        view.isNight = newState
        nightTheme = if (view.isNight) style else Constants.NO_ID.toInt()
        checkDefaultIfEmpty(parentView)
        updateNightModeSchedule(parentView)
        return true
    }

    private fun View.onDefaultClicked() {
        onThemeChange(this, defaultThemeView, R.style.ShikimoriAppTheme_Default)
    }

    private fun View.onDarkClicked() {
        onThemeChange(this, darkThemeView, R.style.ShikimoriAppTheme_Dark)
    }

    private fun View.onAmoledClicked() {
        onThemeChange(this, amoledThemeView, R.style.ShikimoriAppTheme_Amoled)
    }

    private fun View.onDarkLongClick(): Boolean {
        return onNightThemeChange(this, darkThemeView, R.style.ShikimoriAppTheme_Dark)
    }

    private fun View.onAmoledLongClick(): Boolean {
        return onNightThemeChange(this, amoledThemeView, R.style.ShikimoriAppTheme_Amoled)
    }

    private fun updateLightMode(parentView: View) {
        with(parentView) {
            if (!darkThemeView.isNight) darkThemeView.isChecked = false
            if (!amoledThemeView.isNight) amoledThemeView.isChecked = false
            if (!defaultThemeView.isNight) defaultThemeView.isChecked = false
        }
    }

    private fun updateNightMode(parentView: View) {
        with(parentView) {
            if (!darkThemeView.isChecked) darkThemeView.isNight = false
            if (!amoledThemeView.isChecked) amoledThemeView.isNight = false
        }
    }

    private fun checkDefaultIfEmpty(parentView: View) {
        with(parentView) {
            if (!darkThemeView.isChecked && !defaultThemeView.isChecked && !amoledThemeView.isChecked) onDefaultClicked()
        }
    }

    private fun clearNightThemeIfEmpty(parentView: View) {
        with(parentView) {
            if (!darkThemeView.isNight && !defaultThemeView.isNight && !amoledThemeView.isNight) nightTheme = Constants.NO_ID.toInt()
        }
    }

    private fun updateNightModeSchedule(parentView: View) {
        with(parentView) {
            isNightThemePicked.let {
                nightModeLabel.setText(if (it) R.string.settings_theme_night_mode_schedule_title else R.string.settings_theme_night_mode_title)
                nightModeLabel.layoutParams = (nightModeLabel.layoutParams as? LinearLayout.LayoutParams)?.apply { bottomMargin = if (it) context.dp(12) else context.dp(24) }
                TransitionManager.beginDelayedTransition(parentView as ViewGroup, Fade())
                nightTimeStartContainer.visibleIf { it }
                nightTimeEndContainer.visibleIf { it }
            }
        }
    }

    fun setTheme(@StyleRes theme: Int) {
        this.theme = theme

        notifyChanged()
        persistInt(theme)
    }

    fun getTheme(): Int = theme

    fun setNightTheme(@StyleRes theme: Int) {
        this.nightTheme = theme

        notifyChanged()
    }

    fun getNightTheme(): Int = nightTheme

    fun setNightModeStartTime(dayMills: Int) {
        nightStartMills = dayMills

        notifyChanged()
    }

    fun getNightModeStartTime(): Int = nightStartMills

    fun setNightModeEndTime(dayMills: Int) {
        nightEndMills = dayMills

        notifyChanged()
    }

    fun getNightModeEndTime(): Int = nightEndMills

    private val isNightThemePicked: Boolean
        get() = nightTheme != Constants.NO_ID.toInt()
}
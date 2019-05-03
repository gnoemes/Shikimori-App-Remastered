package com.gnoemes.shikimori.presentation.view.common.widget.preferences

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleRes
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.onClick
import kotlinx.android.synthetic.main.view_theme_preference.view.*

class ThemePreference @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleInt: Int = 0
) : Preference(context, attrs, defStyleInt) {

    private var theme: Int = R.style.ShikimoriAppTheme_Default

    init {
        layoutResource = R.layout.view_theme_preference
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInt(index, R.style.ShikimoriAppTheme_Default)
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

            darkThemeView.setOnLongClickListener { updateNightMode(holder.itemView); darkThemeView.isNight = true; true }
            amoledThemeView.setOnLongClickListener { updateNightMode(holder.itemView); amoledThemeView.isNight = true; true }

            when (theme) {
                R.style.ShikimoriAppTheme_Default -> onDefaultClicked()
                R.style.ShikimoriAppTheme_Dark -> onDarkClicked()
                R.style.ShikimoriAppTheme_Amoled -> onAmoledClicked()
            }
        }
    }

    private fun View.onDefaultClicked() {
        updateLightMode(this)
        defaultThemeView.isChecked = true
        theme = R.style.ShikimoriAppTheme_Default
    }

    private fun View.onDarkClicked() {
        updateLightMode(this)
        darkThemeView.isChecked = true
        theme = R.style.ShikimoriAppTheme_Dark
    }

    private fun View.onAmoledClicked() {
        updateLightMode(this)
        amoledThemeView.isChecked = true
        theme = R.style.ShikimoriAppTheme_Amoled
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
            if (!defaultThemeView.isChecked) defaultThemeView.isNight = false
            if (!amoledThemeView.isChecked) amoledThemeView.isNight = false
        }
    }

    fun setTheme(@StyleRes theme: Int) {
        this.theme = theme

        notifyChanged()
        persistInt(theme)
    }

    fun getTheme(): Int = theme
}
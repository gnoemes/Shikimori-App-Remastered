package com.gnoemes.shikimori.presentation.view.common.widget.preferences

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.widget.BaseView
import com.gnoemes.shikimori.utils.drawable
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.view_theme.view.*

class ThemeView @JvmOverloads constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleInt: Int = 0
) : BaseView(context, attrs, defStyleInt) {

    private var theme: Int = DEFAULT

    private var _isChecked: Boolean = false
    private var _isNight: Boolean = false

    private var sunIcon: Drawable? = null
    private var moonIcon: Drawable? = null

    companion object {
        private const val DEFAULT = 0
        private const val DARK = 1
        private const val AMOLED = 2
    }

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ThemeView)
        theme = ta.getInt(R.styleable.ThemeView_appTheme, DEFAULT)
        var iconColor = R.color.default_colorOnSurface
        when (theme) {
            DEFAULT -> {
                themeImageView.setImageResource(R.drawable.ic_theme_default)
                iconColor = R.color.default_colorOnSurface
            }
            DARK -> {
                themeImageView.setImageResource(R.drawable.ic_theme_dark)
                iconColor = R.color.dark_colorOnSurface
            }
            AMOLED -> {
                themeImageView.setImageResource(R.drawable.ic_theme_amoled)
                iconColor = R.color.amoled_colorOnSurface
            }
        }
        ta.recycle()

        sunIcon = context.drawable(R.drawable.ic_sun, iconColor)
        moonIcon = context.drawable(R.drawable.ic_moon, iconColor)
    }

    override fun getLayout(): Int = R.layout.view_theme

    var isChecked: Boolean
        get() = _isChecked
        set(value) {
            _isChecked = value
            _isNight = false
            themeStatusView.visibleIf { value }
            themeStatusView.setImageDrawable(sunIcon)
        }

    var isNight: Boolean
        get() = _isNight
        set(value) {
            _isNight = value
            _isChecked = false
            themeStatusView.visibleIf { value }
            themeStatusView.setImageDrawable(moonIcon)
        }

}
package com.gnoemes.shikimori.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.iterator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.utils.widgets.DebouncedOnClickListener
import com.google.android.material.snackbar.Snackbar

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.isVisible(): Boolean = visibility == View.VISIBLE

fun View.isGone(): Boolean = visibility == View.GONE

inline fun View.visibleIf(block: () -> Boolean) {
    if (block()) visible() else gone()
}

fun View.onClick(mills : Long = Constants.DEFAULT_DEBOUNCE_INTERVAL, l: (v: android.view.View?) -> Unit) {
    setOnClickListener(object : DebouncedOnClickListener(mills) {
        override fun onDebouncedClick(v: View?) {
            l.invoke(v)
        }
    })
}

fun TextView.tintCompoundDrawables(color: Int, pos: Int = 4) {
    val drawables = compoundDrawables

    if (pos < drawables.size) {
        drawables[pos]?.tint(color)
    } else {
        drawables.forEach {
            it?.tint(color)
        }
    }
}

fun SwipeRefreshLayout.showRefresh() {
    isRefreshing = true
}

fun SwipeRefreshLayout.hideRefresh() {
    isRefreshing = false
}

@ColorInt
fun View.color(@ColorRes colorRes: Int): Int = context.color(colorRes)

fun ImageView.tintWithRes(@ColorRes colorRes: Int) = tint(context.color(colorRes))

fun ImageView.tint(@ColorInt colorInt: Int) = setColorFilter(colorInt)

fun ImageView.hasImage() = drawable != null

fun Toolbar.menuVisibleIf(block: () -> Boolean) {
    if (block.invoke()) showMenu()
    else hideMenu()
}

fun Toolbar.hideMenu() {
    this.menu?.iterator()?.forEach { menuItem -> menuItem.isVisible = false }
}

fun Toolbar.showMenu() {
    this.menu?.iterator()?.forEach { menuItem -> menuItem.isVisible = true }
}


fun Snackbar.floatingStyle(context: Context, @DimenRes margins: Int = R.dimen.snackbar_margin, @DrawableRes background: Int = R.drawable.bg_snackbar): Snackbar {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    val margin = context.dimen(margins).toInt()
    params.setMargins(margin, margin, margin, margin)
    this.view.findViewById<Button>(R.id.snackbar_action)?.isAllCaps = false
    this.view.findViewById<TextView>(R.id.snackbar_text)?.setTextColor(context.color(R.color.player_controls))
    this.view.layoutParams = params
    this.view.background = context.drawable(background)
    return this
}




package com.gnoemes.shikimori.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

inline fun View.visibleIf(block: () -> Boolean) {
    if (block()) visible() else gone()
}

fun View.onClick(l: (v: android.view.View?) -> Unit) {
    setOnClickListener(l)
}

@ColorInt
fun View.color(@ColorRes colorRes: Int): Int = context.color(colorRes)

fun ImageView.tintWithRes(@ColorRes colorRes: Int) = tint(context.color(colorRes))

fun ImageView.tint(@ColorInt colorInt: Int) = setColorFilter(colorInt)




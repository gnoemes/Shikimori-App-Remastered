package com.gnoemes.shikimori.utils

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.view.View

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




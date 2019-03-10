package com.gnoemes.shikimori.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes

fun Resources.Theme.attr(@AttrRes attribute: Int): TypedValue {
    val typedValue = TypedValue()
    if (!resolveAttribute(attribute, typedValue, true)) {
        throw IllegalArgumentException("Failed to resolve attribute: $attribute")
    }

    return typedValue
}

@ColorInt
fun Resources.Theme.color(@AttrRes attribute: Int): Int {
    val attr = attr(attribute)
    if (attr.type < TypedValue.TYPE_FIRST_COLOR_INT || attr.type > TypedValue.TYPE_LAST_COLOR_INT) {
        throw IllegalArgumentException("Attribute value type is not color: $attribute")
    }

    return attr.data
}

@DrawableRes
fun Resources.Theme.drawable(@AttrRes attribute: Int): Int {
    return attr(attribute).resourceId
}

@ColorInt
fun Context.colorAttr(@AttrRes attribute: Int): Int = theme.color(attribute)

fun Context.drawableAttr(@AttrRes attribute: Int): Drawable? = drawable(theme.drawable(attribute))

fun Context.attr(@AttrRes attribute: Int): TypedValue = theme.attr(attribute)

@Dimension(unit = Dimension.PX)
fun Context.dimenAttr(@AttrRes attribute: Int): Int =
        TypedValue.complexToDimensionPixelSize(attr(attribute).data, resources.displayMetrics)

@Dimension(unit = Dimension.PX)
fun View.dimenAttr(@AttrRes attribute: Int): Int = context.dimenAttr(attribute)

@ColorInt
fun View.colorAttr(@AttrRes attribute: Int): Int = context.colorAttr(attribute)

fun View.attr(@AttrRes attribute: Int): TypedValue = context.attr(attribute)

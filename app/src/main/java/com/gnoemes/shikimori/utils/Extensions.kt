package com.gnoemes.shikimori.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.gnoemes.shikimori.BuildConfig
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView
import io.reactivex.Single

fun String.appendHostIfNeed(host: String = BuildConfig.ShikimoriBaseUrl): String {
    return if (this.contains("http")) this else host + this
}

fun String.firstUpperCase(): String? {
    return if (TextUtils.isEmpty(this)) null else this.substring(0, 1).toUpperCase() + this.substring(1)
}

fun String?.nullIfEmpty(): String? {
    return if (isNullOrEmpty()) null else this
}

fun String.toBold(): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
            .append(this)
    builder.setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}


fun String.toLink(): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
            .append(this)
    builder.setSpan(URLSpan(""), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}

fun String.toUri() = Uri.parse(this)

fun Boolean.toInt(): Int = if (this) 1 else 0

fun Int.toBoolean(): Boolean? = if (this > 1) null else this == 1

fun Int.unknownIfZero(): String = if (this == 0) "xxx" else toString()

fun Context.inflateLayout(layoutResId: Int): View =
        inflateView(this, layoutResId, null, false)

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup): View =
        inflateLayout(layoutResId, parent, true)

fun Context.dimen(@DimenRes dimen: Int) = this.resources.getDimension(dimen)

fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup, attachToRoot: Boolean): View =
        inflateView(this, layoutResId, parent, attachToRoot)

fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableResId)
}

fun Context.themeDrawable(@DrawableRes drawableResId: Int, @AttrRes attrResId: Int): Drawable? {
    val drawable = ContextCompat.getDrawable(this, drawableResId)
    drawable?.tint(this.colorAttr(attrResId))
    return drawable
}

@ColorInt
fun Context.color(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.calculateColumns(@DimenRes itemWidth: Int): Int {
    val metrics = this.resources.displayMetrics
    return (metrics.widthPixels / dimen(itemWidth)).toInt()
}

fun Drawable.tint(@ColorInt color: Int) {
    DrawableCompat.setTint(this, color)
    mutate()
}

fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToRoot: Boolean = false): View =
        inflateView(context, layoutResId, this, attachToRoot)

fun Toolbar?.addBackButton() {
    this?.navigationIcon = this?.context?.drawable(R.drawable.ic_arrow_back)
}

fun <T> Single<T>.appendLoadingLogic(viewState: BaseView): Single<T> =
        this.doOnSubscribe { viewState.onShowLoading() }
                .doOnSubscribe { viewState.hideEmptyView() }
                .doOnSubscribe { viewState.hideNetworkView() }
                .doAfterTerminate { viewState.onHideLoading() }
                .doOnEvent { _, _ -> viewState.onHideLoading() }

fun <T> Single<T>.appendLightLoadingLogic(viewState: BaseView): Single<T> =
        this.doOnSubscribe { viewState.onShowLightLoading() }
                .doAfterTerminate { viewState.onHideLightLoading() }
                .doOnEvent { _, _ -> viewState.onHideLightLoading() }

inline fun <T : Fragment> T.withArgs(
        argsBuilder: Bundle.() -> Unit): T =
        this.apply {
            arguments = Bundle().apply(argsBuilder)
        }

fun <T : Any> T?.ifNotNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

private fun inflateView(context: Context, layoutResId: Int, parent: ViewGroup?,
                        attachToRoot: Boolean): View =
        LayoutInflater.from(context).inflate(layoutResId, parent, attachToRoot)
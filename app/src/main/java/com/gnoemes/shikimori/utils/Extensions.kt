package com.gnoemes.shikimori.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.*
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gnoemes.shikimori.BuildConfig

fun String.appendHostIfNeed(host: String = BuildConfig.ShikimoriBaseUrl): String {
    return if (this.contains("http")) this else host + this
}

fun String.firstUpperCase(): String? {
    return if (TextUtils.isEmpty(this)) null else this.substring(0, 1).toUpperCase() + this.substring(1)
}

fun String.toUri() = Uri.parse(this)

fun String?.isEmpty(): Boolean = this == null || this.isEmpty()

fun String?.isNotEmpty(): Boolean = this != null && this.isNotEmpty()

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
    return this.resources.getColor(colorRes)
}

fun Context.calculateColumns(@DimenRes itemWidth: Int): Int {
    val metrics = this.resources.displayMetrics
//    val width = metrics.widthPixels / metrics.density
    return (metrics.widthPixels / dimen(itemWidth)).toInt()
}

fun Drawable.tint(@ColorInt color: Int) {
    DrawableCompat.setTint(this, color)
}

fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToRoot: Boolean = false): View =
        inflateView(context, layoutResId, this, attachToRoot)

//@SuppressLint("ResourceType")
//fun Toolbar.addBackButton() {
//    this.navigationIcon = this.context.themeDrawable(R.drawable.ic_arrow_back, R.attr.colorText)
//}

//fun <T> Single<T>.appendLoadingLogic(viewState: BaseView): Single<T> =
//        this.doOnSubscribe { viewState.onShowLoading() }
//                .doAfterTerminate { viewState.onHideLoading() }
//                .doOnEvent { _, _ -> viewState.onHideLoading() }

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
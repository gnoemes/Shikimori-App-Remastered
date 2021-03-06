package com.gnoemes.shikimori.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
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

fun <E> MutableList<E>.clearAndAddAll(items: Collection<E>) {
    clear()
    addAll(items)
}

inline fun <E> MutableList<E>.exist(crossinline block: (E) -> Boolean): Boolean {
    return find { block.invoke(it) } != null
}

fun String.toBold(): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
            .append(this)
    builder.setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}

fun String.toLink(action: String? = ""): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
            .append(this)
    builder.setSpan(URLSpan(action), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}

fun String.colorSpan(@ColorInt color: Int): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
            .append(this)
    builder.setSpan(ForegroundColorSpan(color), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return builder
}

fun String.splitWithSavedNested(startSymbol: Char, endSymbol: Char): Array<String> {
    var depth = 0
    val splitGroups = mutableListOf<String>()

    val part = mutableListOf<Char>()
    var charIndex = 0

    fun addToSplit() {
        splitGroups.add(String(part.toCharArray()))
        part.clear()
    }

    fun append(char: Char) {
        part.add(char)
    }

    this.forEach { char ->

        if (char == startSymbol) {
            depth += 1

            if (depth == 1) addToSplit()

            append(char)
        } else if (char == endSymbol) {
            depth -= 1

            append(char)

            if (depth == 0) addToSplit()

        } else if (charIndex == lastIndex) {
            append(char)
            addToSplit()
        } else append(char)

        charIndex++
    }

    return splitGroups.toTypedArray()
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
    return AppCompatResources.getDrawable(this, drawableResId)
}

fun Context.colorStateList(@ColorRes colorRes: Int): ColorStateList {
    return AppCompatResources.getColorStateList(this, colorRes)
}

fun Context.drawable(@DrawableRes drawableResId: Int, @ColorRes tintColor: Int = 0): Drawable? {
    val drawable = drawable(drawableResId)
    if (tintColor != 0) drawable?.tint(color(tintColor))
    return drawable
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

fun Context.dp(dp: Int): Int {
    return (resources.displayMetrics.density * dp).toInt()
}

fun Drawable.tint(@ColorInt color: Int) {
    DrawableCompat.setTint(this, color)
    mutate()
}

fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToRoot: Boolean = false): View =
        inflateView(context, layoutResId, this, attachToRoot)

fun Toolbar?.addBackButton(@DrawableRes icon: Int = R.drawable.ic_arrow_back, listener: (() -> Unit)? = null) {
    this?.navigationIcon = this?.context?.drawable(icon)
    listener?.let { this?.setNavigationOnClickListener { listener.invoke() } }
}

fun <T> Single<T>.appendLoadingLogic(viewState: BaseView): Single<T> =
        this.doOnSubscribe { viewState.onShowLoading() }
                .doOnSubscribe { viewState.hideEmptyView() }
                .doOnSubscribe { viewState.hideNetworkView() }
                .doOnSubscribe { viewState.showContent(false) }
                .doAfterTerminate { viewState.onHideLoading() }
                .doOnEvent { _, _ -> viewState.onHideLoading() }
                .doOnSuccess { viewState.showContent(true) }

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
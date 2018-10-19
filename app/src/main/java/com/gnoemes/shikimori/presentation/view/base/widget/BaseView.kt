package com.gnoemes.shikimori.presentation.view.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

abstract class BaseView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleInt: Int = 0
) : FrameLayout(context, attrs, defStyleInt) {

    init {
        init(context, attrs)
    }

    protected open fun init(context: Context, attrs: AttributeSet? = null) {
        View.inflate(context, getLayout(), this)
    }

    @LayoutRes
    abstract fun getLayout(): Int
}
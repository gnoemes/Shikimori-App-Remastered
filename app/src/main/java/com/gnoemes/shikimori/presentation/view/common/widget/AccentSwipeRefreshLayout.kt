package com.gnoemes.shikimori.presentation.view.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.colorAttr

class AccentSwipeRefreshLayout @JvmOverloads constructor(context: Context,
                                                         attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    init {
        setColorSchemeColors(context.colorAttr(R.attr.colorAccent))
        setProgressBackgroundColorSchemeColor(context.colorAttr(R.attr.colorPrimary))
    }
}
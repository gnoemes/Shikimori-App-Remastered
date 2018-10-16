package com.gnoemes.shikimori.presentation.view.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.gnoemes.shikimori.R
import kotlinx.android.synthetic.main.view_network_error.view.*

class NetworkErrorView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleInt: Int = 0
) : BaseView(context, attrs, defStyleInt) {

    override fun getLayout(): Int = R.layout.view_network_error

    fun setText(string: String) {
        descriptionTextView.text = string
    }

    fun setText(@StringRes stringRes: Int) {
        descriptionTextView.setText(stringRes)
    }
}
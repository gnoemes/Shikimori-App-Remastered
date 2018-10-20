package com.gnoemes.shikimori.presentation.view.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.widget.BaseView
import kotlinx.android.synthetic.main.view_network_error.view.*

class NetworkErrorView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleInt: Int = 0
) : BaseView(context, attrs, defStyleInt) {

    override fun getLayout(): Int = R.layout.view_network_error


    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.NetworkErrorView)
        descriptionTextView.text = ta.getText(R.styleable.NetworkErrorView_text)
        ta.recycle()
    }

    fun setText(string: String) {
        descriptionTextView.text = string
    }

    fun setText(@StringRes stringRes: Int) {
        descriptionTextView.setText(stringRes)
    }
}
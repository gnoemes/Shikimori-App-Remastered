package com.gnoemes.shikimori.presentation.view.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.view_empty.view.*

class EmptyContentView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleInt: Int = 0
) : BaseView(context, attrs, defStyleInt) {

    lateinit var callback: (View) -> Unit

    override fun getLayout(): Int = R.layout.view_empty

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)
        btnView.setOnClickListener {
            if (::callback.isInitialized) {
                callback.invoke(it)
            }
        }
    }

    fun showButton() {
        btnView.visible()
    }

    fun hideButton() {
        btnView.gone()
    }

    fun setText(text: String) {
        descriptionTextView.text = text
    }

    fun setText(@StringRes textRes: Int) {
        descriptionTextView.setText(textRes)
    }

    fun setButtonText(@StringRes textRes: Int) {
        btnView.setText(textRes)
    }

    fun setButtonText(text: String) {
        btnView.text = text
    }
}
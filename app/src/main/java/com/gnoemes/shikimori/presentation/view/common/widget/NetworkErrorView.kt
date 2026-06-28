package com.gnoemes.shikimori.presentation.view.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ViewNetworkErrorBinding
import com.gnoemes.shikimori.presentation.view.base.widget.BaseView
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.visible

class NetworkErrorView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleInt: Int = 0
) : BaseView(context, attrs, defStyleInt) {

    private lateinit var binding: ViewNetworkErrorBinding
    lateinit var callback: (View) -> Unit

    override fun getLayout(): Int = R.layout.view_network_error

    override fun init(context: Context, attrs: AttributeSet?) {
        binding = ViewNetworkErrorBinding.inflate(android.view.LayoutInflater.from(context), this, true)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.NetworkErrorView)
        binding.descriptionTextView.text = ta.getText(R.styleable.NetworkErrorView_text)
        ta.recycle()

        binding.btnView.setOnClickListener {
            if (::callback.isInitialized) {
                callback.invoke(it)
            }
        }
    }

    fun setText(string: String) {
        binding.descriptionTextView.text = string
    }

    fun setText(@StringRes stringRes: Int) {
        binding.descriptionTextView.setText(stringRes)
    }

    fun showButton() {
        binding.btnView.visible()
    }

    fun hideButton() {
        binding.btnView.gone()
    }

    fun setButtonText(@StringRes textRes: Int) {
        binding.btnView.setText(textRes)
    }

    fun setButtonText(text: String) {
        binding.btnView.text = text
    }
}
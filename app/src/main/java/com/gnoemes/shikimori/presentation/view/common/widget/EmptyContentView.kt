package com.gnoemes.shikimori.presentation.view.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ViewEmptyBinding
import com.gnoemes.shikimori.presentation.view.base.widget.BaseView
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.visible

class EmptyContentView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleInt: Int = 0
) : BaseView(context, attrs, defStyleInt) {

    private lateinit var binding: ViewEmptyBinding
    lateinit var callback: (View) -> Unit

    override fun getLayout(): Int = R.layout.view_empty

    override fun init(context: Context, attrs: AttributeSet?) {
        binding = ViewEmptyBinding.inflate(android.view.LayoutInflater.from(context), this, true)
        binding.btnView.setOnClickListener {
            if (::callback.isInitialized) {
                callback.invoke(it)
            }
        }
    }

    fun showButton() {
        binding.btnView.visible()
    }

    fun hideButton() {
        binding.btnView.gone()
    }

    fun setText(text: String) {
        binding.descriptionTextView.text = text
    }

    fun setText(@StringRes textRes: Int) {
        binding.descriptionTextView.setText(textRes)
    }

    fun setButtonText(@StringRes textRes: Int) {
        binding.btnView.setText(textRes)
    }

    fun setButtonText(text: String) {
        binding.btnView.text = text
    }
}
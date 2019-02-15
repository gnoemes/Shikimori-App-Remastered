package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.fragment.MvpDialogFragment
import com.gnoemes.shikimori.utils.withArgs

class DescriptionDialogFragment : MvpDialogFragment() {

    private var title: String? = null
    private var titleRes: Int = 0
    private var text: String? = null
    private var positiveText: String? = null
    private val defaultPositiveText by lazy { context?.getString(R.string.common_accept) }

    companion object {
        fun newInstance(title: String? = null, titleRes: Int = 0, text: String? = null, positiveText: String? = null) = DescriptionDialogFragment().withArgs {
            putString(ARGUMENT_TITLE, title)
            putInt(ARGUMENT_TITLE_ID, titleRes)
            putString(ARGUMENT_TEXT, text)
            putString(ARGUMENT_POSITIVE_TEXT, positiveText)
        }

        private const val ARGUMENT_TITLE = "ARGUMENT_TITLE"
        private const val ARGUMENT_TITLE_ID = "ARGUMENT_TITLE_ID"
        private const val ARGUMENT_TEXT = "ARGUMENT_TEXT"
        private const val ARGUMENT_POSITIVE_TEXT = "ARGUMENT_POSITIVE_TEXT"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        title = arguments?.getString(ARGUMENT_TITLE, null)
        titleRes = arguments?.getInt(ARGUMENT_TITLE_ID, 0) ?: 0
        text = arguments?.getString(ARGUMENT_TEXT, null)
        positiveText = arguments?.getString(ARGUMENT_POSITIVE_TEXT, null)

        return MaterialDialog(context!!).show {
            if (hasTitle()) title(titleRes, title)
            message(text = text)
            positiveButton(text = positiveText ?: defaultPositiveText)
        }
    }

    private fun hasTitle(): Boolean = title != null || titleRes != 0
}
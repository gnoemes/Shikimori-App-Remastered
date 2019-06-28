package com.gnoemes.shikimori.presentation.view.common.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.dimenAttr
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.dialog_base_bottom_sheet.*
import kotlinx.android.synthetic.main.dialog_description.*

class DescriptionDialogFragment : BaseBottomSheetDialogFragment() {

    private var title: String? = null
    private var titleRes: Int = 0
    private var text: String? = null

    companion object {
        fun newInstance(title: String? = null, titleRes: Int = 0, text: String? = null) = DescriptionDialogFragment().withArgs {
            putString(ARGUMENT_TITLE, title)
            putInt(ARGUMENT_TITLE_ID, titleRes)
            putString(ARGUMENT_TEXT, text)
        }

        private const val ARGUMENT_TITLE = "ARGUMENT_TITLE"
        private const val ARGUMENT_TITLE_ID = "ARGUMENT_TITLE_ID"
        private const val ARGUMENT_TEXT = "ARGUMENT_TEXT"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight =  context.dimenAttr(android.R.attr.actionBarSize)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = arguments?.getString(ARGUMENT_TITLE, null)
        titleRes = arguments?.getInt(ARGUMENT_TITLE_ID, 0) ?: 0
        text = arguments?.getString(ARGUMENT_TEXT, null)

        with(toolbar) {
            this@DescriptionDialogFragment.title?.let { title = it } ?: setTitle(titleRes)
            inflateMenu(R.menu.menu_close)
            setOnMenuItemClickListener { dismiss(); true }
        }

        descriptionView.text = text
    }

    override fun getDialogLayout(): Int = R.layout.dialog_description
}
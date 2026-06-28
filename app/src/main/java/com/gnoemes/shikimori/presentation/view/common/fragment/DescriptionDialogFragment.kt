package com.gnoemes.shikimori.presentation.view.common.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.DialogBaseBottomSheetBinding
import com.gnoemes.shikimori.databinding.DialogDescriptionBinding
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.dimenAttr
import com.gnoemes.shikimori.utils.withArgs

class DescriptionDialogFragment : BaseBottomSheetDialogFragment() {

    private var title: String? = null
    private var titleRes: Int = 0
    private var text: String? = null

    private var baseBinding: DialogBaseBottomSheetBinding? = null
    private var dialogBinding: DialogDescriptionBinding? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseBinding = DialogBaseBottomSheetBinding.inflate(inflater, container, false)
        val bb = baseBinding ?: return null
        dialogBinding = DialogDescriptionBinding.inflate(inflater, bb.fragmentContent, true)
        return bb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = arguments?.getString(ARGUMENT_TITLE, null)
        titleRes = arguments?.getInt(ARGUMENT_TITLE_ID, 0) ?: 0
        text = arguments?.getString(ARGUMENT_TEXT, null)

        baseBinding?.toolbar?.let { toolbar ->
            this@DescriptionDialogFragment.title?.let { toolbar.title = it } ?: toolbar.setTitle(titleRes)
            toolbar.addBackButton(R.drawable.ic_close) { dismiss() }
        }

        dialogBinding?.descriptionView?.text = text
    }

    override fun onDestroyView() {
        baseBinding = null
        dialogBinding = null
        super.onDestroyView()
    }

    override fun getDialogLayout(): Int = R.layout.dialog_description
}
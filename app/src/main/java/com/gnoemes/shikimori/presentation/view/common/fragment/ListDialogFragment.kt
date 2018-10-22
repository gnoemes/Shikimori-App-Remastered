package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.presentation.view.base.fragment.MvpDialogFragment
import com.gnoemes.shikimori.utils.withArgs

class ListDialogFragment : MvpDialogFragment() {

    private var items: List<Pair<String, String>> = emptyList()
    private var title: String? = null
    private var titleRes: Int = Constants.NO_ID.toInt()
    private var idCallback: DialogIdCallback? = null
    private var callback: DialogCallback? = null
    private var isIdCallback: Boolean = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ARGUMENT_ITEMS, items.toTypedArray())
        outState.putInt(ARGUMENT_TITLE_ID, titleRes)
        outState.putString(ARGUMENT_TITLE, title)
    }

    companion object {
        fun newInstance(isIdCallback: Boolean = false) = ListDialogFragment().withArgs { putBoolean(ARGUMENT_IS_IDS, isIdCallback) }
        private const val ARGUMENT_ITEMS = "ARGUMENT_ITEMS"
        private const val ARGUMENT_TITLE_ID = "ARGUMENT_TITLE_ID"
        private const val ARGUMENT_TITLE = "ARGUMENT_TITLE"
        private const val ARGUMENT_IS_IDS = "ARGUMENT_IS_IDS"
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setTitle(titleRes: Int) {
        this.titleRes = titleRes
    }

    fun setItems(items: List<Pair<String, String>>) {
        this.items = items
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        if (savedInstanceState != null) {
            items = (savedInstanceState.getSerializable(ARGUMENT_ITEMS) as Array<Pair<String, String>>).toList()
            titleRes = savedInstanceState.getInt(ARGUMENT_TITLE_ID, Constants.NO_ID.toInt())
            title = savedInstanceState.getString(ARGUMENT_TITLE, "").takeIf { !it.isNullOrBlank() }
        }

        isIdCallback = arguments?.getBoolean(ARGUMENT_IS_IDS, false) ?: false

        idCallback = parentFragment as? DialogIdCallback
        callback = parentFragment as? DialogCallback

        return MaterialDialog(context!!).show {
            if (hasTitle()) title(titleRes, title)
            listItems(items = items.map { it.first }) { _, index, _ ->
                val action = items[index].second
                if (isIdCallback) idCallback?.dialogItemIdCallback(tag, action.toLongOrNull()
                        ?: Constants.NO_ID)
                else callback?.dialogItemCallback(tag, action)
            }
        }
    }

    private fun hasTitle() = title != null || titleRes != Constants.NO_ID.toInt()

    interface DialogIdCallback {
        fun dialogItemIdCallback(tag: String?, id: Long)
    }

    interface DialogCallback {
        fun dialogItemCallback(tag: String?, url: String)
    }
}
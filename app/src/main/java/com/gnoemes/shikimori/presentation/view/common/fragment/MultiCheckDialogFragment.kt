package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.presentation.view.base.fragment.MvpDialogFragment

class MultiCheckDialogFragment : MvpDialogFragment() {

    private var items: List<Pair<Boolean, Pair<String, String>>> = emptyList()
    private var title: String? = null
    private var titleRes: Int = Constants.NO_ID.toInt()
    private var callback: DialogCallback? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ARGUMENT_ITEMS, items.toTypedArray())
        outState.putInt(ARGUMENT_TITLE_ID, titleRes)
        outState.putString(ARGUMENT_TITLE, title)
    }

    companion object {
        fun newInstance() = MultiCheckDialogFragment()
        private const val ARGUMENT_ITEMS = "ARGUMENT_ITEMS"
        private const val ARGUMENT_TITLE_ID = "ARGUMENT_TITLE_ID"
        private const val ARGUMENT_TITLE = "ARGUMENT_TITLE"
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setTitle(titleRes: Int) {
        this.titleRes = titleRes
    }

    fun setItems(items: List<Pair<Boolean, Pair<String, String>>>) {
        this.items = items
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        if (savedInstanceState != null) {
            items = (savedInstanceState.getSerializable(ARGUMENT_ITEMS) as Array<Pair<Boolean, Pair<String, String>>>).toList()
            titleRes = savedInstanceState.getInt(ARGUMENT_TITLE_ID, Constants.NO_ID.toInt())
            title = savedInstanceState.getString(ARGUMENT_TITLE, "").takeIf { !it.isNullOrBlank() }
        }

        callback = parentFragment as? DialogCallback

        return MaterialDialog(context!!).show {
            if (hasTitle()) title(titleRes, title)
            listItemsMultiChoice(
                    items = items.map { it.second.first },
                    initialSelection = items.mapIndexedNotNull { index, pair -> if (pair.first) index else null }.toIntArray(),
                    waitForPositiveButton = true,
                    allowEmptySelection = true)
            { _, indices, _ ->
                val callbackItems = mutableListOf<String>()
                indices.forEach { callbackItems.add(items[it].second.second) }
                callback?.dialogItemCallback(tag, callbackItems)
            }

            positiveButton(res = R.string.common_accept)
            negativeButton(res = R.string.common_cancel)
        }
    }

    private fun hasTitle() = title != null || titleRes != Constants.NO_ID.toInt()

    interface DialogCallback {
        fun dialogItemCallback(tag: String?, items: List<String>)
    }
}
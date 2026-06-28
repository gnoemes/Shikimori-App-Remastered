package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            @Suppress("DEPRECATION")
            val raw = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getSerializable(ARGUMENT_ITEMS, Array::class.java)
            } else {
                savedInstanceState.getSerializable(ARGUMENT_ITEMS)
            } as? Array<Pair<*, *>?>
            items = raw?.toList() as? List<Pair<Boolean, Pair<String, String>>> ?: items
            titleRes = savedInstanceState.getInt(ARGUMENT_TITLE_ID, Constants.NO_ID.toInt())
            title = savedInstanceState.getString(ARGUMENT_TITLE, "").takeIf { !it.isNullOrBlank() }
        }

        callback = parentFragment as? DialogCallback

        return MaterialAlertDialogBuilder(context!!).apply {
            if (hasTitle()) {
                if (title != null) setTitle(title)
                else setTitle(titleRes)
            }
            val checkedItems = mutableListOf<Int>()
            items.forEachIndexed { index, pair -> if (pair.first) checkedItems.add(index) }
            setMultiChoiceItems(
                    items.map { it.second.first }.toTypedArray(),
                    BooleanArray(items.size) { index -> checkedItems.contains(index) }
            ) { _, which, isChecked ->
                if (isChecked) checkedItems.add(which)
                else checkedItems.remove(which)
            }
            setPositiveButton(R.string.common_accept) { _, _ ->
                val callbackItems = mutableListOf<String>()
                checkedItems.forEach { callbackItems.add(items[it].second.second) }
                callback?.dialogItemCallback(this@MultiCheckDialogFragment.tag, callbackItems)
            }
            setNegativeButton(R.string.common_cancel, null)
        }.create()
    }

    private fun hasTitle() = title != null || titleRes != Constants.NO_ID.toInt()

    interface DialogCallback {
        fun dialogItemCallback(tag: String?, items: List<String>)
    }
}
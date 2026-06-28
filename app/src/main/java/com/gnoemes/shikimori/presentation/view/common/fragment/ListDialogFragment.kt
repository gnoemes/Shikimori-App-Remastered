package com.gnoemes.shikimori.presentation.view.common.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            @Suppress("DEPRECATION")
            val raw = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getSerializable(ARGUMENT_ITEMS, Array::class.java)
            } else {
                savedInstanceState.getSerializable(ARGUMENT_ITEMS)
            } as? Array<Pair<String, String>>
            items = raw?.toList() ?: items
            titleRes = savedInstanceState.getInt(ARGUMENT_TITLE_ID, Constants.NO_ID.toInt())
            title = savedInstanceState.getString(ARGUMENT_TITLE, "").takeIf { !it.isNullOrBlank() }
        }

        isIdCallback = arguments?.getBoolean(ARGUMENT_IS_IDS, false) ?: false

        idCallback = parentFragment as? DialogIdCallback
        callback = parentFragment as? DialogCallback

        return MaterialAlertDialogBuilder(context!!).apply {
            if (hasTitle()) {
                if (title != null) setTitle(title)
                else setTitle(titleRes)
            }
            setItems(items.map { it.first }.toTypedArray()) { _, which ->
                val action = items[which].second
                if (isIdCallback) idCallback?.dialogItemIdCallback(this@ListDialogFragment.tag, action.toLongOrNull()
                        ?: Constants.NO_ID)
                else callback?.dialogItemCallback(this@ListDialogFragment.tag, action)
            }
        }.create()
    }

    private fun hasTitle() = title != null || titleRes != Constants.NO_ID.toInt()

    interface DialogIdCallback {
        fun dialogItemIdCallback(tag: String?, id: Long)
    }

    interface DialogCallback {
        fun dialogItemCallback(tag: String?, url: String)
    }
}
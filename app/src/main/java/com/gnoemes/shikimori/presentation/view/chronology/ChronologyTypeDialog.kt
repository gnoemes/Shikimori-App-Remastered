package com.gnoemes.shikimori.presentation.view.chronology

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.chronology.ChronologyType
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.colorStateList
import com.gnoemes.shikimori.utils.dimenAttr
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.dialog_menu.*

class ChronologyTypeDialog : BaseBottomSheetDialogFragment() {

    companion object {
        fun newInstance(type: ChronologyType) = ChronologyTypeDialog().withArgs {
            putSerializable(TYPE_KEY, type)
        }

        private const val TYPE_KEY = "TYPE_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = context.dimenAttr(android.R.attr.actionBarSize)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getDialogLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(toolbar) {
            setTitle(R.string.chronology_type)
        }

        val current = arguments?.getSerializable(TYPE_KEY) as? ChronologyType ?: ChronologyType.MAIN

        val items = ChronologyType.values().toMutableList()

        navView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setItemBackgroundResource(R.drawable.selector_item_menu_background_accent)
                itemTextColor = context.colorStateList(R.color.selector_item_menu_text_color_accent)
            }
            setNavigationItemSelectedListener { menu ->
                (parentFragment as? Callback)?.onTypeChanged(items.first { it.ordinal == menu.itemId })
                dismiss()
                true
            }
            menu.apply {
                val checkedId = items.find { it == current }?.ordinal ?: 0
                items.forEach { add(0, it.ordinal, it.ordinal, getTypeText(it)) }
                setGroupCheckable(0, true, true)
                setCheckedItem(checkedId)
            }
        }
    }

    private fun getTypeText(it: ChronologyType): CharSequence? = when (it) {
        ChronologyType.MAIN -> context?.getString(R.string.chronology_type_main)
        ChronologyType.LINKED_DIRECTLY -> context?.getString(R.string.chronology_type_linked)
    }


    override fun getDialogLayout(): Int = R.layout.dialog_menu

    interface Callback {
        fun onTypeChanged(newType: ChronologyType)
    }
}
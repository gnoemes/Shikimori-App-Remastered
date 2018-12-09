package com.gnoemes.shikimori.presentation.view.base.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.getCurrentTheme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomSheetDialogFragment : MvpDialogFragment() {

    protected lateinit var bottomSheet: FrameLayout
    var autoExpand = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val view = inflater.inflate(R.layout.dialog_base_bottom_sheet, container, false)
        if (getDialogLayout() != android.view.View.NO_ID) {
            inflater.inflate(getDialogLayout(), view.findViewById(R.id.fragment_content), true)
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(ContextThemeWrapper(context!!, R.style.Theme_MaterialComponents_BottomSheetDialog), context!!.getCurrentTheme)
                .apply {
                    setOnShowListener {
                        bottomSheet = (it as BottomSheetDialog).findViewById(R.id.design_bottom_sheet)!!
                        bottomSheet.layoutParams = bottomSheet.layoutParams.apply { height = ViewGroup.LayoutParams.MATCH_PARENT }

                        it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        if (autoExpand) {
                            expandDialog()
                        }
                    }
                }
    }


    protected open fun expandDialog() {
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
    }

    protected open fun collapseDialog() {
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
    }

    @LayoutRes
    abstract fun getDialogLayout(): Int
}
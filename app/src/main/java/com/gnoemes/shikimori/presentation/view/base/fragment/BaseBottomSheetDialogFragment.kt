package com.gnoemes.shikimori.presentation.view.base.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.getCurrentAscentTheme
import com.gnoemes.shikimori.utils.wrapTheme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomSheetDialogFragment : MvpDialogFragment() {

    protected lateinit var bottomSheet: FrameLayout
    open var autoExpand = true

    var peekHeight = -1

    private val viewHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val view = inflater.inflate(R.layout.dialog_base_bottom_sheet, container, false)
        if (getDialogLayout() != android.view.View.NO_ID) {
            inflater.inflate(getDialogLayout(), view.findViewById(R.id.fragment_content), true)
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(context!!.wrapTheme(R.style.Theme_MaterialComponents_BottomSheetDialog), context!!.getCurrentAscentTheme)
                .apply {
                    isCancelable = true
                    setCanceledOnTouchOutside(true)
                    setOnShowListener {
                        bottomSheet = (it as BottomSheetDialog).findViewById(R.id.design_bottom_sheet)!!
                        bottomSheet.background = ColorDrawable(Color.TRANSPARENT)
                        if (peekHeight == -1) bottomSheet.layoutParams = bottomSheet.layoutParams.apply { height = ViewGroup.LayoutParams.MATCH_PARENT }
                        else BottomSheetBehavior.from(bottomSheet).peekHeight = peekHeight

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

    override fun onDestroyView() {
        viewHandler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    @LayoutRes
    abstract fun getDialogLayout(): Int

    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post { action.invoke() }
    }
}
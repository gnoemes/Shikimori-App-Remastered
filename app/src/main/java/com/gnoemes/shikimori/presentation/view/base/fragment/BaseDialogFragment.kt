package com.gnoemes.shikimori.presentation.view.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.getCurrentAscentTheme
import com.gnoemes.shikimori.utils.inputMethodManager
import com.gnoemes.shikimori.utils.wrapTheme
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseDialogFragment : MvpDialogFragment(), BaseFragmentView {

    private val viewHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getDialogLayout(), container, false)

    //TODO mb don't use bottom sheet
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(context!!.wrapTheme(R.style.Theme_MaterialComponents_BottomSheetDialog), context!!.getCurrentAscentTheme)
                .apply {
                    setOnShowListener {
                        (it as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)?.apply {
                            layoutParams = (layoutParams as CoordinatorLayout.LayoutParams).apply { behavior = null }
                        }
                        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                    }
                }
    }

    override fun onBackPressed() = dismiss()

    override fun hideSoftInput() {
        activity?.inputMethodManager()?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    @LayoutRes
    abstract fun getDialogLayout(): Int

    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post { action.invoke() }
    }

    override fun setTitle(title: String) {
    }

    override fun setTitle(stringRes: Int) {
    }
}
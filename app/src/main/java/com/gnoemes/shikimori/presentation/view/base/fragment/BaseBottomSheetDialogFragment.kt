package com.gnoemes.shikimori.presentation.view.base.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.DialogBaseBottomSheetBinding
import com.gnoemes.shikimori.utils.attr
import com.gnoemes.shikimori.utils.drawable
import com.gnoemes.shikimori.utils.getCurrentAscentTheme
import com.gnoemes.shikimori.utils.wrapTheme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomSheetDialogFragment : MvpDialogFragment() {

    protected lateinit var bottomSheet: FrameLayout
    open var autoExpand = true

    var peekHeight = -1

    private val viewHandler = Handler()
    private var _binding: DialogBaseBottomSheetBinding? = null
    private val binding: DialogBaseBottomSheetBinding? get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        _binding = DialogBaseBottomSheetBinding.inflate(inflater, container, false)
        val b = _binding ?: return null
        if (getDialogLayout() != android.view.View.NO_ID) {
            inflater.inflate(getDialogLayout(), b.fragmentContent, true)
        }
        return b.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(context!!.wrapTheme(com.google.android.material.R.style.Theme_MaterialComponents_BottomSheetDialog), context!!.getCurrentAscentTheme)
                .apply {
                    isCancelable = true
                    setCanceledOnTouchOutside(true)
                    setOnShowListener {
                        bottomSheet = (it as BottomSheetDialog).findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                        if (peekHeight != -1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            @Suppress("DEPRECATION")
                            it.window?.statusBarColor = Color.TRANSPARENT
                        }

                        bottomSheet.background = context.drawable(windowBackground)

                        if (peekHeight == -1) bottomSheet.layoutParams = bottomSheet.layoutParams.apply { height = ViewGroup.LayoutParams.MATCH_PARENT }
                        else BottomSheetBehavior.from(bottomSheet).peekHeight = peekHeight

                        it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                        if (autoExpand) {
                            expandDialog()
                        }

                        BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
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
        _binding = null
        super.onDestroyView()
    }

    @LayoutRes
    abstract fun getDialogLayout(): Int

    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post { action.invoke() }
    }

    protected open val windowBackground by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) R.drawable.bg_rate_dialog_window
        else context!!.attr(R.attr.editRateBackground).resourceId
    }
}
package com.gnoemes.shikimori.presentation.view.base.activity

import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    /**
     * Hide keyboard
     */
    fun hideSoftInput()

    /**
     * Show loading Dialog
     */
    fun onShowLoading()

    /**
     * Hide loading dialog
     */
    fun onHideLoading()

    /**
     * Set title
     */
    fun setTitle(title: String)

    /**
     * Set title
     */
    fun setTitle(@StringRes stringRes: Int)

}
package com.gnoemes.shikimori.presentation.view.base.activity

import androidx.annotation.StringRes
import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    /**
     * Hide keyboard
     */
    fun hideSoftInput()

    /**
     * Show loading Dialog
     */
    fun onShowLoading() = Unit

    /**
     * Hide loading dialog
     */
    fun onHideLoading() = Unit

    /**
     * Set title
     */
    fun setTitle(title: String)

    /**
     * Set title
     */
    fun setTitle(@StringRes stringRes: Int)

    fun showEmptyView() = Unit

    fun hideEmptyView() = Unit

    fun showNetworkView() = Unit

    fun hideNetworkView() = Unit

    fun onShowLightLoading() = Unit

    fun onHideLightLoading() = Unit
}
package com.gnoemes.shikimori.presentation.view.base.fragment

import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView

interface BaseFragmentView : BaseNetworkView {
    fun onBackPressed()
}
package com.gnoemes.shikimori.presentation.view.base.fragment

import android.content.Context
import com.gnoemes.shikimori.presentation.presenter.base.BasePresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import javax.inject.Provider

abstract class BaseBottomSheetInjectionDialogFragment<Presenter : BasePresenter<View>, View : BaseNetworkView> : BaseBottomSheetDialogFragment(), BaseFragmentView {

    @Inject
    lateinit var presenterProvider: Provider<Presenter>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    abstract val presenter : Presenter

    override fun onBackPressed() {
        dismiss()
    }

    override fun hideSoftInput() = Unit
    override fun setTitle(title: String) = Unit
    override fun setTitle(stringRes: Int) = Unit
}
package com.gnoemes.shikimori.presentation.view.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.presenter.base.BasePresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.inputMethodManager
import com.gnoemes.shikimori.utils.visible
import com.gnoemes.shikimori.utils.visibleIf
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject
import javax.inject.Provider

abstract class BaseFragment<Presenter : BasePresenter<View>, View : BaseNetworkView>
    : MvpFragment(), BaseFragmentView {

    @Inject
    lateinit var presenterProvider: Provider<Presenter>

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val view = inflater.inflate(R.layout.fragment_base, container, false)
        if (getFragmentLayout() != android.view.View.NO_ID) {
            inflater.inflate(getFragmentLayout(), view.findViewById(R.id.fragment_content), true)
        }
        return view
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkErrorView?.setText(R.string.common_error_message_without_pull)
        networkErrorView?.gone()
        emptyContentView?.gone()
    }

    override fun onDestroyView() {
        hideSoftInput()
        super.onDestroyView()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    protected abstract fun getPresenter(): Presenter

    @LayoutRes
    protected abstract fun getFragmentLayout(): Int

    ///////////////////////////////////////////////////////////////////////////
    // UI METHODS
    ///////////////////////////////////////////////////////////////////////////


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                getPresenter().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hideSoftInput() {
        activity?.inputMethodManager()?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun onBackPressed() = getPresenter().onBackPressed()

    override fun setTitle(title: String) {
        toolbar?.title = title
    }

    override fun setTitle(stringRes: Int) {
        toolbar?.setTitle(stringRes)
    }

    override fun onShowLoading() {
        progressBar?.visible()
        fragment_content?.gone()
    }

    override fun onHideLoading() {
        progressBar?.gone()
        fragment_content?.visible()
    }

    override fun onShowLightLoading() {
        progressBar?.visible()
    }

    override fun onHideLightLoading() {
        progressBar?.gone()
    }

    override fun showNetworkView(block: Boolean) {
        networkErrorView?.visible()
        fragment_content?.visibleIf { !block }
    }

    override fun hideNetworkView() {
        networkErrorView?.gone()
        fragment_content?.visible()
    }

    override fun showEmptyView() {
        emptyContentView?.visible()
    }

    override fun hideEmptyView() {
        emptyContentView?.gone()
    }
}
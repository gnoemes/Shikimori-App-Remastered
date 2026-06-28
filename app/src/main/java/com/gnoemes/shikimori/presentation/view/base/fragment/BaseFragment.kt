package com.gnoemes.shikimori.presentation.view.base.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import moxy.MvpAppCompatFragment
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentBaseBinding
import com.gnoemes.shikimori.databinding.LayoutDefaultPlaceholdersBinding
import com.gnoemes.shikimori.databinding.LayoutProgressBinding
import com.gnoemes.shikimori.databinding.LayoutToolbarBinding
import com.gnoemes.shikimori.presentation.presenter.base.BasePresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseNetworkView
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.inputMethodManager
import com.gnoemes.shikimori.utils.visible
import com.gnoemes.shikimori.utils.visibleIf
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import javax.inject.Provider

abstract class BaseFragment<Presenter : BasePresenter<V>, V : BaseNetworkView>
    : MvpAppCompatFragment(), BaseFragmentView {

    @Inject
    lateinit var presenterProvider: Provider<Presenter>

    private val viewHandler = Handler(Looper.getMainLooper())

    private var _baseBinding: FragmentBaseBinding? = null
    protected val baseBinding: FragmentBaseBinding? get() = _baseBinding

    private var _toolbarBinding: LayoutToolbarBinding? = null
    protected val toolbarBinding: LayoutToolbarBinding?
        get() = _toolbarBinding

    private var _placeholdersBinding: LayoutDefaultPlaceholdersBinding? = null
    protected val placeholdersBinding: LayoutDefaultPlaceholdersBinding?
        get() = _placeholdersBinding

    private var _progressBinding: LayoutProgressBinding? = null
    protected val progressBinding: LayoutProgressBinding?
        get() = _progressBinding

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Initialize base bindings without adding the base layout to the container.
     * Used by subclasses that manage their own root view hierarchy (e.g. BaseDetailsFragment).
     */
    protected fun initBaseBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        val b = _baseBinding ?: return
        _toolbarBinding = LayoutToolbarBinding.bind(b.root.findViewById(R.id.included_layout_toolbar))
        _placeholdersBinding = LayoutDefaultPlaceholdersBinding.bind(b.root.findViewById(R.id.coordinator))
        _progressBinding = LayoutProgressBinding.bind(b.root.findViewById(R.id.included_layout_progress))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        val b = _baseBinding ?: return null
        
        if (getFragmentLayout() != View.NO_ID) {
            inflater.inflate(getFragmentLayout(), b.fragmentContent, true)
        }
        
        _toolbarBinding = LayoutToolbarBinding.bind(b.root.findViewById(R.id.included_layout_toolbar))
        _placeholdersBinding = LayoutDefaultPlaceholdersBinding.bind(b.root.findViewById(R.id.coordinator))
        _progressBinding = LayoutProgressBinding.bind(b.root.findViewById(R.id.included_layout_progress))

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _placeholdersBinding?.let {
            it.networkErrorView.setText(R.string.common_error_message_without_pull)
            it.networkErrorView.gone()
            it.emptyContentView.gone()
        }
    }

    override fun onDestroyView() {
        hideSoftInput()
        viewHandler.removeCallbacksAndMessages(null)
        _baseBinding = null
        _toolbarBinding = null
        _placeholdersBinding = null
        _progressBinding = null
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

    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post { action.invoke() }
    }

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
        toolbarBinding?.toolbar?.title = title
    }

    override fun setTitle(stringRes: Int) {
        toolbarBinding?.toolbar?.setTitle(stringRes)
    }

    override fun showContent(show: Boolean) {
        baseBinding?.fragmentContent?.visibleIf { show }
    }

    override fun onShowLoading() {
        progressBinding?.progressBar?.visible()
    }

    override fun onHideLoading() {
        progressBinding?.progressBar?.gone()
    }

    override fun onShowLightLoading() {
        progressBinding?.progressBar?.visible()
    }

    override fun onHideLightLoading() {
        progressBinding?.progressBar?.gone()
    }

    override fun showNetworkView() {
        _placeholdersBinding?.networkErrorView?.visible()
    }

    override fun hideNetworkView() {
        _placeholdersBinding?.networkErrorView?.gone()
    }

    override fun showEmptyView() {
        _placeholdersBinding?.emptyContentView?.visible()
    }

    override fun hideEmptyView() {
        _placeholdersBinding?.emptyContentView?.gone()
    }
}

package com.gnoemes.shikimori.presentation.view.more

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentMoreBinding
import com.gnoemes.shikimori.presentation.presenter.more.MorePresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.fragment.AuthDialog
import com.gnoemes.shikimori.presentation.view.more.adapter.MoreAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import javax.inject.Inject

class MoreFragment : BaseFragment<MorePresenter, MoreView>(), MoreView, AuthDialog.AuthCallback {

    private var _viewBinding: FragmentMoreBinding? = null
    private val viewBinding: FragmentMoreBinding? get() = _viewBinding

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var morePresenter: MorePresenter

    @ProvidePresenter
    fun providePresenter(): MorePresenter {
        morePresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            morePresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        return morePresenter
    }

    companion object {
        fun newInstance() = MoreFragment()
    }

    private val moreAdapter by lazy { MoreAdapter(imageLoader) { getPresenter().onCategoryClicked(it) } }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentMoreBinding.bind(view.findViewById(R.id.fragment_content))
        val vb = _viewBinding ?: return

        with(vb.recyclerView) {
            adapter = moreAdapter
            layoutManager = LinearLayoutManager(context)
        }

        toolbarBinding?.toolbar?.gone()
        placeholdersBinding?.emptyContentView?.gone()
        placeholdersBinding?.networkErrorView?.gone()
        progressBinding?.progressBar?.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): MorePresenter = morePresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_more

    ///////////////////////////////////////////////////////////////////////////
    // Callbacks
    ///////////////////////////////////////////////////////////////////////////
    override fun onSignIn() {
        getPresenter().onSignIn()
    }

    override fun onSignUp() {
        getPresenter().onSignUp()
    }

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<Any>) {
        moreAdapter.bindItems(items)
    }

    override fun showAuthDialog() {
        val tag = "AuthDialog"
        val dialog = parentFragmentManager.findFragmentByTag(tag)
        if (dialog == null) {
            parentFragmentManager.setFragmentResultListener(AuthDialog.AUTH_REQUEST_KEY, this) { _, bundle ->
                when (bundle.getString(AuthDialog.AUTH_ACTION_KEY)) {
                    AuthDialog.ACTION_SIGN_IN -> onSignIn()
                    AuthDialog.ACTION_SIGN_UP -> onSignUp()
                }
            }
            AuthDialog().show(parentFragmentManager, tag)
        }
    }
}
package com.gnoemes.shikimori.presentation.view.more

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.presenter.more.MorePresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.fragment.AuthDialog
import com.gnoemes.shikimori.presentation.view.more.adapter.MoreAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class MoreFragment : BaseFragment<MorePresenter, MoreView>(), MoreView, AuthDialog.AuthCallback {

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

        with(recyclerView) {
            adapter = moreAdapter
            layoutManager = LinearLayoutManager(context)
        }

        toolbar?.gone()
        emptyContentView?.gone()
        networkErrorView?.gone()
        progressBar?.gone()
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
        val dialog = fragmentManager?.findFragmentByTag(tag)
        if (dialog == null) {
            AuthDialog().apply {
                setTargetFragment(this@MoreFragment, 42)
            }.show(fragmentManager!!, tag)
        }
    }
}
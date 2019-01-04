package com.gnoemes.shikimori.presentation.view.shikimorimain

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.presentation.presenter.shikimorimain.ShikimoriMainPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.topic.TopicListFragment
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.ifNotNull
import kotlinx.android.synthetic.main.fragment_shikimori_main.*
import kotlinx.android.synthetic.main.layout_appbar_tabs.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ShikimoriMainFragment : BaseFragment<ShikimoriMainPresenter, ShikimoriMainView>(), ShikimoriMainView {

    @InjectPresenter
    lateinit var mainPresenter: ShikimoriMainPresenter

    @ProvidePresenter
    fun providePresenter(): ShikimoriMainPresenter {
        mainPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            mainPresenter.localRouter = (it as RouterProvider).localRouter
        }

        return mainPresenter
    }

    companion object {
        fun newInstance() = ShikimoriMainFragment()
    }

    private val adapter by lazy { PagerAdapter(childFragmentManager) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.gone()

        pagesContainerView.adapter = adapter
        pagesContainerView.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(pagesContainerView)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): ShikimoriMainPresenter = mainPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_shikimori_main

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    //Todo change mocks
    inner class PagerAdapter(
            fm: FragmentManager
    ) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TopicListFragment()
                1 -> TopicListFragment()
                else -> TopicListFragment()
            }
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> context?.getString(R.string.topic_news)
                1 -> context?.getString(R.string.topic_my_feed)
                else -> context?.getString(R.string.topic_forum)
            }
        }
    }
}
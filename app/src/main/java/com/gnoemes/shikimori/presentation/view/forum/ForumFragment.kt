package com.gnoemes.shikimori.presentation.view.forum

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.presentation.presenter.forum.ForumPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.forum.adapter.ForumAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.hideRefresh
import com.gnoemes.shikimori.utils.showRefresh
import com.gnoemes.shikimori.utils.visibleIf
import kotlinx.android.synthetic.main.layout_default_list.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ForumFragment : BaseFragment<ForumPresenter, ForumView>(), ForumView {

    @InjectPresenter
    lateinit var forumPresenter: ForumPresenter

    @ProvidePresenter
    fun providePresenter(): ForumPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
        }
    }

    companion object {
        fun newInstance() = ForumFragment()
    }

    private val adapter by lazy { ForumAdapter(getPresenter()::onForumClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.gone()

        with(recyclerView) {
            adapter = this@ForumFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
        networkErrorView.setText(R.string.common_error_message)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): ForumPresenter = forumPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_forum

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<Forum>) {
        adapter.bindItems(items)
    }

    override fun showContent(show: Boolean) {
        recyclerView.visibleIf { show }
    }

    override fun onShowLoading() = refreshLayout.showRefresh()
    override fun onHideLoading() = refreshLayout.hideRefresh()
}
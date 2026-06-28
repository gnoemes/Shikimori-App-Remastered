package com.gnoemes.shikimori.presentation.view.forum

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentForumBinding
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.presentation.presenter.forum.ForumPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.forum.adapter.ForumAdapter
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.hideRefresh
import com.gnoemes.shikimori.utils.showRefresh
import com.gnoemes.shikimori.utils.visibleIf

class ForumFragment : BaseFragment<ForumPresenter, ForumView>(), ForumView {

    private var _viewBinding: FragmentForumBinding? = null
    private val viewBinding: FragmentForumBinding? get() = _viewBinding

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
        _viewBinding = FragmentForumBinding.bind(view.findViewById(R.id.fragment_content))
        val vb = _viewBinding ?: return

        toolbarBinding?.toolbar?.gone()

        with(vb.includedLayoutDefaultList.recyclerView) {
            adapter = this@ForumFragment.adapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        vb.includedLayoutDefaultList.refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
        placeholdersBinding?.networkErrorView?.setText(R.string.common_error_message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
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
        viewBinding?.includedLayoutDefaultList?.recyclerView?.visibleIf { show }
    }

    override fun onShowLoading() { viewBinding?.includedLayoutDefaultList?.refreshLayout?.showRefresh() }
    override fun onHideLoading() { viewBinding?.includedLayoutDefaultList?.refreshLayout?.hideRefresh() }
}
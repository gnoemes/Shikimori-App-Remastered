package com.gnoemes.shikimori.presentation.view.friends

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentDefaultListBinding
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.user.presentation.FriendViewModel
import com.gnoemes.shikimori.presentation.presenter.friends.FriendsPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.friends.adapter.FriendsAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import javax.inject.Inject

class FriendsFragment : BaseFragment<FriendsPresenter, FriendsView>(), FriendsView {

    private var _viewBinding: FragmentDefaultListBinding? = null
    private val viewBinding: FragmentDefaultListBinding? get() = _viewBinding

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var friendsPresenter: FriendsPresenter

    @ProvidePresenter
    fun providePresenter(): FriendsPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            id = arguments!!.getLong(AppExtras.ARGUMENT_USER_ID)
        }
    }

    companion object {
        fun newInstance(id : Long) = FriendsFragment().withArgs { putLong(AppExtras.ARGUMENT_USER_ID, id) }
    }

    private val adapter by lazy { FriendsAdapter(imageLoader, getPresenter()::onContentClicked).apply { if (!hasObservers()) setHasStableIds(true) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentDefaultListBinding.bind(view.findViewById(R.id.fragment_content))
        val vb = _viewBinding ?: return

        toolbarBinding?.toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
            setTitle(R.string.common_friends)
        }

        with(vb.includedLayoutDefaultList.recyclerView) {
            adapter = this@FriendsFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        vb.includedLayoutDefaultList.refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): FriendsPresenter = friendsPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_default_list

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<FriendViewModel>) {
        adapter.bindItems(items)
    }

    override fun showFriendsCount(count: Int) {
        toolbarBinding?.toolbar?.menu?.add("$count")
        toolbarBinding?.toolbar?.menu?.getItem(0)?.apply {
            isEnabled = false
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
    }

    override fun showContent(show: Boolean) {
        viewBinding?.includedLayoutDefaultList?.recyclerView?.visibleIf { show }
    }

    override fun onShowLoading() { viewBinding?.includedLayoutDefaultList?.refreshLayout?.showRefresh() }

    override fun onHideLoading() { viewBinding?.includedLayoutDefaultList?.refreshLayout?.hideRefresh() }
}

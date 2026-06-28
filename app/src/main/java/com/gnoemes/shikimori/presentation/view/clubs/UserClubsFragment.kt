package com.gnoemes.shikimori.presentation.view.clubs

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentDefaultListBinding
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import com.gnoemes.shikimori.presentation.presenter.clubs.UserClubsPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.clubs.adapter.UserClubAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import javax.inject.Inject

class UserClubsFragment : BaseFragment<UserClubsPresenter, UserClubsView>(), UserClubsView {

    private var _binding: FragmentDefaultListBinding? = null
    private val binding: FragmentDefaultListBinding? get() = _binding

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var clubsPresenter: UserClubsPresenter

    @ProvidePresenter
    fun providePresenter(): UserClubsPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            id = arguments!!.getLong(AppExtras.ARGUMENT_USER_ID)
        }
    }

    companion object {
        fun newInstance(id: Long) = UserClubsFragment().withArgs { putLong(AppExtras.ARGUMENT_USER_ID, id) }
    }

    private val adapter by lazy { UserClubAdapter(imageLoader, getPresenter()::onContentClicked).apply { if (!hasObservers()) setHasStableIds(true) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDefaultListBinding.bind(view.findViewById(R.id.fragment_content))
        val b = _binding ?: return

        toolbarBinding?.toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
            setTitle(R.string.common_clubs)
        }

        with(b.includedLayoutDefaultList.recyclerView) {
            adapter = this@UserClubsFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        b.includedLayoutDefaultList.refreshLayout.setOnRefreshListener { getPresenter().onRefresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): UserClubsPresenter = clubsPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_default_list

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<UserClubViewModel>) {
        adapter.bindItems(items)
    }

    override fun showClubsCount(count: Int) {
        toolbarBinding?.toolbar?.menu?.add("$count")
        toolbarBinding?.toolbar?.menu?.getItem(0)?.apply {
            isEnabled = false
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
    }

    override fun showContent(show: Boolean) {
        binding?.includedLayoutDefaultList?.recyclerView?.visibleIf { show }
    }

    override fun onShowLoading() { binding?.includedLayoutDefaultList?.refreshLayout?.showRefresh() }

    override fun onHideLoading() { binding?.includedLayoutDefaultList?.refreshLayout?.hideRefresh() }

}
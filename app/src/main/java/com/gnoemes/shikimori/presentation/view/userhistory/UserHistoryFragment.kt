package com.gnoemes.shikimori.presentation.view.userhistory

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.LayoutDefaultListBinding
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryNavigationData
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryViewModel
import com.gnoemes.shikimori.presentation.presenter.userhistory.UserHistoryPresenter
import com.gnoemes.shikimori.presentation.view.base.adapter.BasePaginationAdapter
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.userhistory.adapter.UserHistoryAdapter
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.colorAttr
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs
import javax.inject.Inject

class UserHistoryFragment : BasePaginationFragment<UserHistoryViewModel, UserHistoryPresenter, UserHistoryView>(), UserHistoryView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var historyPresenter: UserHistoryPresenter


    @ProvidePresenter
    fun providePresenter(): UserHistoryPresenter {
        return presenterProvider.get().apply {
            localRouter = (parentFragment as RouterProvider).localRouter
            id = arguments!!.getLong(AppExtras.ARGUMENT_USER_ID)
            name = arguments!!.getString(USER_NAME, "")
        }
    }

    companion object {
        fun newInstance(data: UserHistoryNavigationData) = UserHistoryFragment().withArgs {
            putLong(AppExtras.ARGUMENT_USER_ID, data.id)
            putString(USER_NAME, data.name)
        }

        private const val USER_NAME = "USER_NAME"
    }

    private val historyAdapter by lazy { UserHistoryAdapter(imageLoader, getPresenter()::onContentClicked) }

    private lateinit var historyListBinding: LayoutDefaultListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        historyListBinding = LayoutDefaultListBinding.bind(view.findViewById(R.id.included_layout_default_list))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBinding?.toolbar?.apply {
            setTitle(R.string.common_user_history)
            addBackButton { getPresenter().onBackPressed() }
        }

        with(historyListBinding.recyclerView) {
            adapter = this@UserHistoryFragment.adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalSpaceItemDecorator(context.dimen(R.dimen.margin_normal).toInt(), true, 0))
            background = ColorDrawable(context.colorAttr(R.attr.colorPrimary))
            addOnScrollListener(nextPageListener)
        }
    }

    override fun onDestroyView() {
        historyListBinding.recyclerView.removeOnScrollListener(nextPageListener)
        super.onDestroyView()
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): UserHistoryPresenter = historyPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_default_list

    override val adapter: BasePaginationAdapter
        get() = historyAdapter

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setTitle(title: String) {
        val text = String.format(context!!.getString(R.string.profile_history_format), title)
        toolbarBinding?.toolbar?.title = text
    }
}
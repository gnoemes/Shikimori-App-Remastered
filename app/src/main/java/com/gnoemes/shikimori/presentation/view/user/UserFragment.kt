package com.gnoemes.shikimori.presentation.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.user.presentation.UserContentViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserHeadViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserInfoViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserRateViewModel
import com.gnoemes.shikimori.presentation.presenter.user.UserPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.user.adapter.UserFavoriteContentAdapter
import com.gnoemes.shikimori.presentation.view.user.adapter.UserProfileContentAdapter
import com.gnoemes.shikimori.presentation.view.user.holders.UserContentViewHolder
import com.gnoemes.shikimori.presentation.view.user.holders.UserInfoViewHolder
import com.gnoemes.shikimori.presentation.view.user.holders.UserRateViewHolder
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.layout_default_placeholders.*
import kotlinx.android.synthetic.main.layout_profile_auth.*
import kotlinx.android.synthetic.main.layout_user_profile_info_content.*
import kotlinx.android.synthetic.main.layout_user_profile_toolbar.*
import javax.inject.Inject


class UserFragment : BaseFragment<UserPresenter, UserView>(), UserView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @InjectPresenter
    lateinit var userPresenter: UserPresenter

    @ProvidePresenter
    fun providePresenter(): UserPresenter =
            presenterProvider.get().apply {
                localRouter = (parentFragment as RouterProvider).localRouter
                id = arguments?.getLong(AppExtras.ARGUMENT_USER_ID, Constants.NO_ID)
                        ?: Constants.NO_ID
            }

    companion object {
        fun newInstance(id: Long) = UserFragment().withArgs { putLong(AppExtras.ARGUMENT_USER_ID, id) }
        fun newInstance() = UserFragment()
    }

    private val maxHeight by lazy { (appBarLayout.height - toolbar.height).toFloat() }
    private val primaryColor by lazy { context?.colorAttr(R.attr.colorPrimary)!! }

    private val favoritesAdapter by lazy { UserFavoriteContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val friendsAdapter by lazy { UserProfileContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val clubsAdapter by lazy { UserProfileContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }

    private var infoHolder: UserInfoViewHolder? = null
    private var animeRateHolder: UserRateViewHolder? = null
    private var mangaRateHolder: UserRateViewHolder? = null

    private var favoritesHolder: UserContentViewHolder? = null
    private var friendsHolder: UserContentViewHolder? = null
    private var clubsHolder: UserContentViewHolder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.apply {
            addBackButton { getPresenter().onBackPressed() }
        }

        infoHolder = UserInfoViewHolder(infoLayout, getPresenter()::onAction)
        animeRateHolder = UserRateViewHolder(animeRateLayout, true, getPresenter()::onAction, getPresenter()::onArrowClicked)
        mangaRateHolder = UserRateViewHolder(mangaRateLayout, false, getPresenter()::onAction, getPresenter()::onArrowClicked)

        appBarLayout.addOnOffsetChangedListener(appbarOffsetListener)

        networkErrorView.callback = { getPresenter().onRefresh() }
        networkErrorView.showButton()

        signUpBtn.onClick { getPresenter().onSignUp() }
        signInBtn.onClick { getPresenter().onSignIn() }
        authLayout.gone()
    }

    private val appbarOffsetListener = AppBarLayout.OnOffsetChangedListener { _, offset ->
        val percent = 1 - (-offset / maxHeight)

        lastOnlineView.alpha = percent
        nameView.alpha = percent
        avatarView.alpha = percent
        avatarCollapsedView.alpha = 1 - percent
        nameCollapsedView.alpha = 1 - percent
        val alpha = 255 - (255 * percent).toInt()
        toolbar.setBackgroundColor(ColorUtils.setAlphaComponent(primaryColor, if (alpha < 0) 0 else if (alpha > 255) 255 else alpha))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        appBarLayout.removeOnOffsetChangedListener(appbarOffsetListener)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): UserPresenter = userPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_user_profile

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setHead(data: UserHeadViewModel) {
        lastOnlineView.text = data.lastOnline
        nameView.text = data.name
        nameCollapsedView.text = data.name

        imageLoader.setCircleImage(avatarView, data.image.x160)
        imageLoader.setCircleImage(avatarCollapsedView, data.image.x160)
    }

    override fun setInfo(data: UserInfoViewModel) {
        infoHolder?.bind(data)
    }

    override fun setAnimeRate(data: UserRateViewModel) {
        animeRateHolder?.bind(data)
    }

    override fun setMangaRate(data: UserRateViewModel) {
        mangaRateHolder?.bind(data)
    }

    override fun setFavorites(isMe: Boolean, it: UserContentViewModel) {
        val layout = if (isMe) thirdContentLayout else firstContentLayout
        favoritesHolder = UserContentViewHolder(layout, favoritesAdapter)
        favoritesHolder?.bind(it)
    }

    override fun setFriends(isMe: Boolean, it: UserContentViewModel) {
        val layout = if (isMe) firstContentLayout else secondContentLayout
        friendsHolder = UserContentViewHolder(layout, friendsAdapter)
        friendsHolder?.bind(it)
    }

    override fun setClubs(isMe: Boolean, it: UserContentViewModel) {
        val layout = if (isMe) secondContentLayout else thirdContentLayout
        clubsHolder = UserContentViewHolder(layout, clubsAdapter)
        clubsHolder?.bind(it)
    }

    override fun showContent(show: Boolean) {
        scrollView.visibleIf { show }
        appBarLayout.visibleIf { show }
    }

    override fun toggleAnimeRate(expanded: Boolean) {
        animeRateHolder?.toggle(expanded)
    }

    override fun toggleMangaRate(expanded: Boolean) {
        mangaRateHolder?.toggle(expanded)
    }

    override fun addSettings() {
        with(toolbar) {
            navigationIcon = null
            inflateMenu(R.menu.menu_user)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_settings -> getPresenter().onSettingsClicked()
                }
                true
            }
        }
    }

    override fun showAuthView(show: Boolean) {
        authLayout.visibleIf { show }
        appBarLayout.visible()
    }

    override fun showNetworkView() = networkErrorView.visible()
    override fun hideNetworkView() = networkErrorView.gone()
}
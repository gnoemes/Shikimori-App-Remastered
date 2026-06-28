package com.gnoemes.shikimori.presentation.view.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentUserProfileBinding
import com.gnoemes.shikimori.databinding.LayoutDefaultPlaceholdersBinding
import com.gnoemes.shikimori.databinding.LayoutProfileAuthBinding
import com.gnoemes.shikimori.databinding.LayoutUserProfileInfoContentBinding
import com.gnoemes.shikimori.databinding.LayoutUserProfileToolbarBinding
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
import com.gnoemes.shikimori.utils.colorAttr
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.material.appbar.AppBarLayout
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

    private var _profileBinding: FragmentUserProfileBinding? = null
    private val profileBinding: FragmentUserProfileBinding? get() = _profileBinding

    private var userToolbarBinding: LayoutUserProfileToolbarBinding? = null
    private var userPlaceholdersBinding: LayoutDefaultPlaceholdersBinding? = null
    private var authBinding: LayoutProfileAuthBinding? = null
    private var infoContentBinding: LayoutUserProfileInfoContentBinding? = null

    private val maxHeight by lazy {
        val utb = userToolbarBinding ?: return@lazy 0f
        (utb.appBarLayout.height - utb.toolbar.height).toFloat()
    }
    private val primaryColor by lazy { requireContext().colorAttr(R.attr.colorPrimary) }

    private val favoritesAdapter by lazy { UserFavoriteContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val friendsAdapter by lazy { UserProfileContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val clubsAdapter by lazy { UserProfileContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }

    private var infoHolder: UserInfoViewHolder? = null
    private var animeRateHolder: UserRateViewHolder? = null
    private var mangaRateHolder: UserRateViewHolder? = null

    private var favoritesHolder: UserContentViewHolder? = null
    private var friendsHolder: UserContentViewHolder? = null
    private var clubsHolder: UserContentViewHolder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _profileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)

        val appBarLayout = profileBinding!!.root.findViewById<AppBarLayout>(R.id.included_layout_user_profile_toolbar)
        userToolbarBinding = LayoutUserProfileToolbarBinding.bind(appBarLayout)
        userPlaceholdersBinding = LayoutDefaultPlaceholdersBinding.bind(profileBinding!!.coordinator)
        authBinding = LayoutProfileAuthBinding.bind(profileBinding!!.authLayout.root)
        infoContentBinding = LayoutUserProfileInfoContentBinding.bind(profileBinding!!.infoLayout.infoContent.root)

        return profileBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userToolbarBinding?.toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
        }

        infoHolder = UserInfoViewHolder.create(profileBinding!!.infoLayout.root, getPresenter()::onAction)
        animeRateHolder = UserRateViewHolder.create(profileBinding!!.animeRateLayout.root, true, getPresenter()::onAction, getPresenter()::onArrowClicked)
        mangaRateHolder = UserRateViewHolder.create(profileBinding!!.mangaRateLayout.root, false, getPresenter()::onAction, getPresenter()::onArrowClicked)

        userToolbarBinding?.appBarLayout?.addOnOffsetChangedListener(appbarOffsetListener)

        userPlaceholdersBinding?.networkErrorView?.callback = { getPresenter().onRefresh() }

        authBinding?.signUpBtn?.onClick { getPresenter().onSignUp() }
        authBinding?.signInBtn?.onClick { getPresenter().onSignIn() }
        authBinding?.root?.gone()
        userPlaceholdersBinding?.networkErrorView?.gone()
    }

    private val appbarOffsetListener = AppBarLayout.OnOffsetChangedListener { _, offset ->
        val percent = 1 - (-offset / maxHeight)

        infoContentBinding?.lastOnlineView?.alpha = percent
        infoContentBinding?.nameView?.alpha = percent
        infoContentBinding?.avatarView?.alpha = percent
        userToolbarBinding?.avatarCollapsedView?.alpha = 1 - percent
        userToolbarBinding?.nameCollapsedView?.alpha = 1 - percent
        val alpha = 255 - (255 * percent).toInt()
        userToolbarBinding?.toolbar?.setBackgroundColor(ColorUtils.setAlphaComponent(primaryColor, if (alpha < 0) 0 else if (alpha > 255) 255 else alpha))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userToolbarBinding?.appBarLayout?.removeOnOffsetChangedListener(appbarOffsetListener)
        _profileBinding = null
        userToolbarBinding = null
        userPlaceholdersBinding = null
        authBinding = null
        infoContentBinding = null
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
        infoContentBinding?.lastOnlineView?.text = data.lastOnline
        infoContentBinding?.nameView?.text = data.name
        userToolbarBinding?.nameCollapsedView?.text = data.name

        infoContentBinding?.avatarView?.let { imageLoader.setCircleImage(it, data.image.x160) }
        userToolbarBinding?.avatarCollapsedView?.let { imageLoader.setCircleImage(it, data.image.x160) }
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
        val b = profileBinding ?: return
        val layout = if (isMe) b.thirdContentLayout.root else b.firstContentLayout.root
        favoritesHolder = UserContentViewHolder.create(layout, favoritesAdapter)
        favoritesHolder?.bind(it)
    }

    override fun setFriends(isMe: Boolean, it: UserContentViewModel) {
        val b = profileBinding ?: return
        val layout = if (isMe) b.firstContentLayout.root else b.secondContentLayout.root
        friendsHolder = UserContentViewHolder.create(layout, friendsAdapter)
        friendsHolder?.bind(it)
    }

    override fun setClubs(isMe: Boolean, it: UserContentViewModel) {
        val b = profileBinding ?: return
        val layout = if (isMe) b.secondContentLayout.root else b.thirdContentLayout.root
        clubsHolder = UserContentViewHolder.create(layout, clubsAdapter)
        clubsHolder?.bind(it)
    }

    override fun showContent(show: Boolean) {
        val b = profileBinding ?: return
        b.scrollView.visibleIf { show }
        if (show) userPlaceholdersBinding?.emptyContentView?.gone()
        if (userToolbarBinding?.toolbar?.navigationIcon == null) userToolbarBinding?.appBarLayout?.visible()
        else userToolbarBinding?.appBarLayout?.visibleIf { show }
    }

    override fun toggleAnimeRate(expanded: Boolean) {
        animeRateHolder?.toggle(expanded)
    }

    override fun toggleMangaRate(expanded: Boolean) {
        mangaRateHolder?.toggle(expanded)
    }

    override fun addSettings() {
        userToolbarBinding?.toolbar?.apply {
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

    override fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun showAuthView(show: Boolean) {
        authBinding?.root?.visibleIf { show }
        userToolbarBinding?.appBarLayout?.visible()
        if (show) userPlaceholdersBinding?.networkErrorView?.gone()
    }

    override fun showNetworkView() {
        userPlaceholdersBinding?.networkErrorView?.visible()
        authBinding?.root?.gone()
    }

    override fun hideNetworkView() { userPlaceholdersBinding?.networkErrorView?.gone() }
}
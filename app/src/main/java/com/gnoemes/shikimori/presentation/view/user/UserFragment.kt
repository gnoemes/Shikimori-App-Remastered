package com.gnoemes.shikimori.presentation.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.user.presentation.*
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
                id = arguments!!.getLong(AppExtras.ARGUMENT_USER_ID)
            }

    companion object {
        fun newInstance(id: Long) = UserFragment().withArgs { putLong(AppExtras.ARGUMENT_USER_ID, id) }
    }

    private val expandedAvatarSize by lazy { context?.dimen(R.dimen.image_profile_big_height)!! }
    private val collapseAvatarSize by lazy { context?.dp(40)!! }
    private val maxHeight by lazy { (appBarLayout.height - toolbar.height).toFloat() }
    private val avatarStartXPosition by lazy { appBarLayout.width / 2 - expandedAvatarSize / 2 }
    private val avatarStartYPosition by lazy { context?.dimenAttr(android.R.attr.actionBarSize)!! }
    private val avatarEndXPosition by lazy { context?.dp(40)!! }
    private val avatarEndYPosition by lazy { maxHeight - context?.dp(46)!! - avatarStartYPosition }

    private val favoritesAdapter by lazy { UserFavoriteContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val friendsAdapter by lazy { UserProfileContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }
    private val clubsAdapter by lazy { UserProfileContentAdapter(imageLoader, getPresenter()::onContentClicked, getPresenter()::onAction) }

    private lateinit var infoHolder: UserInfoViewHolder
    private lateinit var animeRateHolder: UserRateViewHolder
    private lateinit var mangaRateHolder: UserRateViewHolder

    private lateinit var favoritesHolder: UserContentViewHolder
    private lateinit var friendsHolder: UserContentViewHolder
    private lateinit var clubsHolder: UserContentViewHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getFragmentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.apply {
            addBackButton { getPresenter().onBackPressed() }
            inflateMenu(R.menu.menu_user)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_history -> getPresenter().onAction(UserProfileAction.History)
                }
                true
            }
        }

        infoHolder = UserInfoViewHolder(infoLayout, getPresenter()::onAction)
        animeRateHolder = UserRateViewHolder(animeRateLayout, true, getPresenter()::onAction)
        mangaRateHolder = UserRateViewHolder(mangaRateLayout, false, getPresenter()::onAction)

        appBarLayout.addOnOffsetChangedListener(appbarOffsetListener)

        networkErrorView.callback = {getPresenter().onRefresh()}
        networkErrorView.showButton()
    }

    private val appbarOffsetListener = AppBarLayout.OnOffsetChangedListener { _, offset ->
        val percent = 1 - (-offset / maxHeight)

        lastOnlineView.alpha = percent

        avatarView.layoutParams = avatarView.layoutParams.apply {
            val newSize = (collapseAvatarSize + (expandedAvatarSize - collapseAvatarSize) * percent).toInt()
            width = newSize
            height = newSize
        }

        avatarView.translationY = (avatarStartYPosition + avatarEndYPosition) * (1 - percent)
        avatarView.translationX = (avatarEndXPosition - avatarStartXPosition) * (1 - percent)
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
        collapsingToolbar.title = data.name
        lastOnlineView.text = data.lastOnline

        if (!avatarView.hasImage()) imageLoader.setCircleImage(avatarView, data.image.x160)
    }

    override fun setInfo(data: UserInfoViewModel) {
        infoHolder.bind(data)
    }

    override fun setAnimeRate(data: UserRateViewModel) {
        animeRateHolder.bind(data)
    }

    override fun setMangaRate(data: UserRateViewModel) {
        mangaRateHolder.bind(data)
    }

    override fun setFavorites(isMe: Boolean, it: UserContentViewModel) {
        val layout = if (isMe) thirdContentLayout else firstContentLayout
        favoritesHolder = UserContentViewHolder(layout, favoritesAdapter)
        favoritesHolder.bind(it)
    }

    override fun setFriends(isMe: Boolean, it: UserContentViewModel) {
        val layout = if (isMe) firstContentLayout else secondContentLayout
        friendsHolder = UserContentViewHolder(layout, friendsAdapter)
        friendsHolder.bind(it)
    }

    override fun setClubs(isMe: Boolean, it: UserContentViewModel) {
        val layout = if (isMe) secondContentLayout else thirdContentLayout
        clubsHolder = UserContentViewHolder(layout, clubsAdapter)
        clubsHolder.bind(it)
    }

    override fun showContent(show: Boolean) {
        scrollView.visibleIf { show }
        appBarLayout.visibleIf { show }
    }
    override fun showNetworkView() = networkErrorView.visible()
    override fun hideNetworkView() = networkErrorView.gone()
}
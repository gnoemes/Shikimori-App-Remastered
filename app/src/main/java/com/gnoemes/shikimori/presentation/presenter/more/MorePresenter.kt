package com.gnoemes.shikimori.presentation.presenter.more

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.more.MoreCategory
import com.gnoemes.shikimori.entity.more.MoreProfileItem
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.more.provider.MoreResourceProvider
import com.gnoemes.shikimori.presentation.view.more.MoreView
import javax.inject.Inject

@InjectViewState
class MorePresenter @Inject constructor(
        private val userInteractor: UserInteractor,
        private val resourceProvider: MoreResourceProvider
) : BaseNetworkPresenter<MoreView>() {

    private val items = mutableListOf<Any>()
    private var user: UserBrief? = null

    override fun initData() {
        loadList()
        loadUser()
    }

    private fun loadList() {
        items.addAll(resourceProvider.getMoreItems())
        viewState.showData(items)
    }

    private fun loadUser() =
            userInteractor.getMyUserBrief()
                    .subscribe(this::setUser, this::processErrors)

    private fun setUser(user: UserBrief) {
        this.user = user

        val first = items.firstOrNull()
        if (first is MoreProfileItem && first.status == UserStatus.GUEST) {
            items[0] = MoreProfileItem(UserStatus.AUTHORIZED, user.name, user.image.x148)
            viewState.showData(items)
        }
    }

    fun onCategoryClicked(category: MoreCategory) {
    }
}
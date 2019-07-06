package com.gnoemes.shikimori.presentation.presenter.userhistory

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.user.UserInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BasePaginationPresenter
import com.gnoemes.shikimori.presentation.presenter.userhistory.conveter.UserHistoryViewModelConverter
import com.gnoemes.shikimori.presentation.view.userhistory.UserHistoryView
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class UserHistoryPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val converter: UserHistoryViewModelConverter
) : BasePaginationPresenter<UserHistoryViewModel, UserHistoryView>() {

    var id: Long = Constants.NO_ID
    var name = ""

    override fun initData() {
        super.initData()

        if (name.isNotBlank()) viewState.setTitle(name)
    }

    override fun getPaginatorRequestFactory(): (Int) -> Single<List<UserHistoryViewModel>> =
            { page -> interactor.getHistory(id, page).map(converter) }

    override fun showData(show: Boolean, data: List<UserHistoryViewModel>) {
        if (show) viewState.showData(converter.groupItems(data))
        viewState.showContent(show)
    }
}
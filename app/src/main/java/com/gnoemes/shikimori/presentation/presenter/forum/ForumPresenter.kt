package com.gnoemes.shikimori.presentation.presenter.forum

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.topic.TopicInteractor
import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.forum.ForumView
import com.gnoemes.shikimori.presentation.view.forum.converter.ForumConverter
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class ForumPresenter @Inject constructor(
        private val interactor: TopicInteractor,
        private val converter: ForumConverter
) : BaseNetworkPresenter<ForumView>() {

    override fun initData() {
        loadData()
    }

    private fun loadData() =
            interactor.getForums()
                    .appendLoadingLogic(viewState)
                    .map(converter)
                    .subscribe(this::showData, this::processErrors)

    private fun showData(items: List<Forum>) {
        viewState.showData(items)
    }

    fun onForumClicked(type: ForumType) {
        router.navigateTo(Screens.TOPICS, type)
        logEvent(AnalyticEvent.NAVIGATION_TOPIC_LIST)
    }

    fun onRefresh() {
        loadData()
    }
}
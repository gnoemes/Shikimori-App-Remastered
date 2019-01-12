package com.gnoemes.shikimori.presentation.presenter.topic.list

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.domain.topic.TopicInteractor
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.presenter.base.BasePaginationPresenter
import com.gnoemes.shikimori.presentation.presenter.topic.converter.TopicViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.presentation.view.topic.list.TopicListView
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class TopicListPresenter @Inject constructor(
        private val interactor: TopicInteractor,
        private val converter: TopicViewModelConverter,
        private val topicResourceProvider: TopicResourceProvider
) : BasePaginationPresenter<TopicViewModel, TopicListView>() {

    lateinit var type: ForumType

    override fun initData() {
        super.initData()

        viewState.setTitle(topicResourceProvider.getTopicName(type))
    }

    override fun getPaginatorRequestFactory(): (Int) -> Single<List<TopicViewModel>> {
        return { page: Int -> interactor.getList(type, page).map(converter) }
    }

}
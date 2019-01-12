package com.gnoemes.shikimori.presentation.view.topic.list

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragmentView

interface TopicListView : BaseFragmentView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: List<Any>)

    fun hideData()

    fun showPageLoading()

    fun hidePageLoading()

}
package com.gnoemes.shikimori.presentation.view.topic.details

import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.topic.presentation.TopicContentViewModel
import com.gnoemes.shikimori.entity.topic.presentation.TopicUserViewModel
import com.gnoemes.shikimori.presentation.view.base.fragment.BasePaginationView

interface TopicView : BasePaginationView {

    fun setUserData(item : TopicUserViewModel)

    fun setContentData(item : TopicContentViewModel)

    fun setCommentsText(text : String?)

    fun showCommentsMore(show : Boolean)

    fun showPreviousComments(show : Boolean)

    fun setCommentsCount(count : Long)

    fun showCommentsLoading(show : Boolean)

    fun setLinkedContent(linked: LinkedContent?)

}
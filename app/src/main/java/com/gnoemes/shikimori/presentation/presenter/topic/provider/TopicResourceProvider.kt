package com.gnoemes.shikimori.presentation.presenter.topic.provider

import androidx.annotation.ColorRes
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.domain.TopicType

interface TopicResourceProvider {

    fun getTopicName(type : ForumType) : String

    @ColorRes
    fun getTagColor(type: TopicType) : Int
}
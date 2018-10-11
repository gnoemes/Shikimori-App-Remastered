package com.gnoemes.shikimori.data.repository.topic.converter

import com.gnoemes.shikimori.entity.topic.data.TopicResponse
import com.gnoemes.shikimori.entity.topic.domain.Topic
import io.reactivex.functions.Function

interface TopicResponseConverter : Function<List<TopicResponse>, List<Topic>> {

    fun convertResponse(it: TopicResponse?): Topic?
}
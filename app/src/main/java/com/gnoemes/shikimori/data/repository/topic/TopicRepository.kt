package com.gnoemes.shikimori.data.repository.topic

import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.domain.Topic
import io.reactivex.Single

interface TopicRepository {

    fun getList(page: Int, limit: Int, type: ForumType): Single<List<Topic>>

    fun getDetails(id: Long): Single<Topic>

    fun getForums(): Single<List<Forum>>
}
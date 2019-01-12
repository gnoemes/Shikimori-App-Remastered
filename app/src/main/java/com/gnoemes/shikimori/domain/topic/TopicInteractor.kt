package com.gnoemes.shikimori.domain.topic

import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.domain.Topic
import io.reactivex.Single

interface TopicInteractor {

    fun getList(type: ForumType, page: Int, limit: Int = Constants.DEFAULT_LIMIT): Single<List<Topic>>

    fun getDetails(id: Long): Single<Topic>

    fun getForums(): Single<List<Forum>>

}
package com.gnoemes.shikimori.domain.topic

import com.gnoemes.shikimori.data.repository.topic.TopicRepository
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.domain.Topic
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class TopicInteractorImpl @Inject constructor(
        private val repository: TopicRepository
) : TopicInteractor {

    override fun getList(page: Int, limit: Int, type: ForumType): Single<List<Topic>> =
            repository.getList(page, limit, type)
                    .applyErrorHandlerAndSchedulers()

    override fun getDetails(id: Long): Single<Topic> =
            repository.getDetails(id)
                    .applyErrorHandlerAndSchedulers()

    override fun getForums(): Single<List<Forum>> =
            repository.getForums()
                    .applyErrorHandlerAndSchedulers()
}
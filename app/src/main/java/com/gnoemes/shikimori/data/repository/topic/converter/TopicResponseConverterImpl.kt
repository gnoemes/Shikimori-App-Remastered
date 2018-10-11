package com.gnoemes.shikimori.data.repository.topic.converter

import com.gnoemes.shikimori.data.repository.common.LinkedContentResponseConverter
import com.gnoemes.shikimori.data.repository.user.converter.UserBriefResponseConverter
import com.gnoemes.shikimori.entity.topic.data.TopicResponse
import com.gnoemes.shikimori.entity.topic.domain.Topic
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import javax.inject.Inject

class TopicResponseConverterImpl @Inject constructor(
        private val forumConverter: ForumResponseConverter,
        private val userConverter: UserBriefResponseConverter,
        private val linkedConverter: LinkedContentResponseConverter
) : TopicResponseConverter {

    override fun apply(t: List<TopicResponse>): List<Topic> = t.map { convertResponse(it)!! }

    override fun convertResponse(it: TopicResponse?): Topic? {
        if (it == null) {
            return null
        }


        return Topic(
                it.id,
                it.title,
                it.description,
                it.descriptionHtml,
                it.footer,
                it.dateCreated,
                it.commentsCount,
                forumConverter.convertResponse(it.forum),
                userConverter.convertResponse(it.user)!!,
                convertType(it.type, it.description, it.descriptionHtml),
                it.linkedType,
                linkedConverter.convertResponse(it.linked),
                it.isViewed
        )
    }

    private fun convertType(type: String?, description: String?, html: String?): TopicType {
        if (type.isNullOrEmpty()) {
            return TopicType.DEFAULT
        }

        return TopicType.values()
                .filter { it.isEqualType(type) }
                .map {
                    when {
                        (it == TopicType.NEWS || it == TopicType.NEWS_LINK_ONLY) && description.isNullOrEmpty() && html.isNullOrEmpty() -> TopicType.NEWS_LINK_ONLY
                        else -> it
                    }
                }.firstOrNull() ?: TopicType.DEFAULT

    }
}
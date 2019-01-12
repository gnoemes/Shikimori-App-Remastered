package com.gnoemes.shikimori.presentation.presenter.topic.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.topic.domain.Topic
import com.gnoemes.shikimori.entity.topic.domain.TopicEvent
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import com.gnoemes.shikimori.entity.topic.presentation.TopicContentViewModel
import com.gnoemes.shikimori.entity.topic.presentation.TopicUserViewModel
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import javax.inject.Inject

class TopicViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val dateTimeConverter: DateTimeConverter,
        private val textProcessor: BBCodesTextProcessor,
        private val resourceProvider: TopicResourceProvider
) : TopicViewModelConverter {

    override fun apply(t: List<Topic>): List<TopicViewModel> =
            t.map { convertTopic(it) }

    private fun convertTopic(it: Topic): TopicViewModel {
        val createdDate = dateTimeConverter.convertDateAgoToString(it.dateCreated)

        val tag = getTag(it.type) ?: getTag(it.event, it.episode)
        val color = resourceProvider.getTagColor(it.type)
        val tagColor = if (color == 0) getTagColor(it.event) else color

        val userViewModel = TopicUserViewModel(it.user, createdDate, tag, tagColor)
        val contentViewModel = TopicContentViewModel(it.title, it.description?.let { textProcessor.process(it) })
        R.color.topic_new_episode
        return TopicViewModel(
                it.id,
                userViewModel,
                contentViewModel,
                it.commentsCount.toInt(),
                it.type,
                it.linked,
                it.isViewed,
                it.event,
                it.episode
        )
    }

    private fun getTagColor(event: TopicEvent): Int {
        return when (event) {
            TopicEvent.RELEASED -> R.color.topic_release
            TopicEvent.EPISODE -> R.color.topic_new_episode
            else -> 0
        }
    }

    private fun getTag(event: TopicEvent, episode: String?): String? {
        return when {
            event == TopicEvent.EPISODE || !episode.isNullOrBlank() -> "$episode ${context.getString(R.string.topic_tag_episode)}"
            event == TopicEvent.RELEASED -> context.getString(R.string.topic_tag_release)
            else -> null
        }
    }

    private fun getTag(type: TopicType): String? {
        return when (type) {
            TopicType.NEWS, TopicType.NEWS_LINK_ONLY -> context.getString(R.string.topic_tag_news)
            TopicType.COLLECTION -> context.getString(R.string.topic_tag_collection)
            TopicType.COSPLAY -> context.getString(R.string.topic_tag_cosplay)
            TopicType.REVIEW -> context.getString(R.string.topic_tag_review)
            else -> null
        }
    }
}
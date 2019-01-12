package com.gnoemes.shikimori.presentation.presenter.topic.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.topic.domain.Topic
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import javax.inject.Inject

class TopicViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val dateTimeConverter: DateTimeConverter,
        private val textProcessor: BBCodesTextProcessor
) : TopicViewModelConverter {

    override fun apply(t: List<Topic>): List<TopicViewModel> =
            t.map { convertTopic(it) }

    private fun convertTopic(it: Topic): TopicViewModel {
        val createdDate = dateTimeConverter.convertDateAgoToString(it.dateCreated)

        return TopicViewModel(
                it.id,
                it.user,
                createdDate,
                getTag(it.type),
                it.title,
                it.description?.let { textProcessor.process(it) },
                it.descriptionHtml,
                it.footer,
                it.commentsCount.toInt(),
                it.type,
                it.linked,
                it.isViewed,
                it.episode
        )
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
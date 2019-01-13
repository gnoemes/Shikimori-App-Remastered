package com.gnoemes.shikimori.presentation.view.topic.details.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.comment.domain.Comment
import com.gnoemes.shikimori.entity.comment.presentation.CommentViewModel
import com.gnoemes.shikimori.entity.topic.presentation.TopicUserViewModel
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import javax.inject.Inject

class CommentViewModelConverterImpl @Inject constructor(
        private val context: Context,
        private val dateTimeConverter: DateTimeConverter,
        private val textProcessor: BBCodesTextProcessor
) : CommentViewModelConverter {

    override fun apply(t: List<Comment>): List<CommentViewModel> =
            t.map { convertComment(it) }

    override fun convertComment(it: Comment): CommentViewModel {
        val createdDate = dateTimeConverter.convertDateAgoToString(it.dateCreated)

        val tag = getTag(it.isOfftopic, it.isSummary)
        val tagColor = getTagColor(it.isOfftopic, it.isSummary)?.let { context.color(it) }
        val userData = TopicUserViewModel(it.user, createdDate, tag, tagColor)
        val content = it.body?.let { textProcessor.process(it) }


        return CommentViewModel(
                it.id,
                it.userId,
                it.commentableId,
                it.commentableType,
                userData,
                content,
                it.isOfftopic,
                it.isSummary,
                it.isEditable
        )
    }

    private fun getTagColor(offtopic: Boolean, summary: Boolean): Int? {
        return when {
            offtopic -> R.color.comment_offtopic
            summary -> R.color.comment_review
            else -> null
        }
    }

    private fun getTag(offtopic: Boolean, summary: Boolean): String? {
        return when {
            offtopic -> context.getString(R.string.topic_tag_offtopic)
            summary -> context.getString(R.string.topic_tag_summary)
            else -> null
        }
    }
}
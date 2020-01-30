package com.gnoemes.shikimori.presentation.presenter.topic.provider

import android.content.Context
import androidx.annotation.ColorRes
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import javax.inject.Inject

class TopicResourceProviderImpl @Inject constructor(
        private val context: Context
) : TopicResourceProvider {

    override fun getTopicName(type: ForumType): String {
        return when (type) {
            ForumType.ALL -> context.getString(R.string.forum_all)
            ForumType.CLUBS -> context.getString(R.string.common_clubs)
            ForumType.GAMES -> context.getString(R.string.forum_games)
            ForumType.CONTESTS -> context.getString(R.string.forum_contests)
            ForumType.MY_CLUBS -> context.getString(R.string.forum_my_clubs)
            ForumType.NEWS -> context.getString(R.string.forum_news)
            ForumType.REVIEWS -> context.getString(R.string.forum_reviews)
            ForumType.VISUAL_NOVELS -> context.getString(R.string.forum_vn)
            ForumType.OFF_TOPIC -> context.getString(R.string.forum_off_topic)
            ForumType.SITE -> context.getString(R.string.forum_site)
            ForumType.COLLECTIONS -> context.getString(R.string.forum_collection)
            ForumType.COSPLAY -> context.getString(R.string.forum_cosplay)
            ForumType.ANIME_AND_MANGA -> context.getString(R.string.forum_animanga)
            ForumType.ARTICLES -> context.getString(R.string.forum_articles)
        }
    }

    @ColorRes
    override fun getTagColor(type: TopicType): Int {
        return when (type) {
            TopicType.NEWS, TopicType.NEWS_LINK_ONLY -> R.color.topic_news
            TopicType.COLLECTION -> R.color.topic_collection
            TopicType.COSPLAY -> R.color.topic_cosplay
            TopicType.REVIEW -> R.color.topic_review
            else -> 0
        }
    }

    override fun getCommentsMoreText(limit: Int, diff: Int?): String {
        return if (diff == null) {
            context.getString(R.string.comment_more_format_message_last) + " " + context.resources.getQuantityString(R.plurals.commentaries, limit, limit)
        } else {
            val firstPart = String.format(context.getString(R.string.comment_more_format_message), limit)
            firstPart + " " + context.resources.getQuantityString(R.plurals.commentaries, diff, diff)
        }
    }
}
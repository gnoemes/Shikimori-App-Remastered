package com.gnoemes.shikimori.presentation.view.forum.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import javax.inject.Inject

class ForumConverterImpl @Inject constructor(
        private val context: Context
) : ForumConverter {

    override fun apply(t: List<Forum>): List<Forum> {
        val list = t.toMutableList()
        list.add(0, Forum(0, "", ForumType.ALL, ""))
        return list.map { convertForum(it) }
    }

    private fun convertForum(it: Forum): Forum {
        val localizedName = context.getString(getLocalizedName(it.type))
        return it.copy(name = localizedName)
    }

    private fun getLocalizedName(type: ForumType): Int {
        return when (type) {
            ForumType.ALL -> R.string.forum_all
            ForumType.MY_CLUBS -> R.string.forum_my_clubs
            ForumType.NEWS -> R.string.forum_news
            ForumType.ANIME_AND_MANGA -> R.string.forum_animanga
            ForumType.CLUBS -> R.string.forum_clubs
            ForumType.COLLECTIONS -> R.string.forum_collection
            ForumType.CONTESTS -> R.string.forum_contests
            ForumType.COSPLAY -> R.string.forum_cosplay
            ForumType.GAMES -> R.string.forum_games
            ForumType.OFF_TOPIC -> R.string.forum_off_topic
            ForumType.REVIEWS -> R.string.forum_reviews
            ForumType.SITE -> R.string.forum_site
            ForumType.VISUAL_NOVELS -> R.string.forum_vn
            ForumType.ARTICLES -> R.string.forum_articles
        }
    }
}
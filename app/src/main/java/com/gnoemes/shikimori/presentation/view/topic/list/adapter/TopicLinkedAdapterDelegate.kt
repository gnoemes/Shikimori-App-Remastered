package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.domain.TopicEvent
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_topic_linked.view.*

class TopicLinkedAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val converter: DateTimeConverter,
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<TopicViewModel, Any, TopicLinkedAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is TopicViewModel && item.type == TopicType.NEWS_LINK_ONLY

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_topic_linked))

    override fun onBindViewHolder(item: TopicViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: TopicViewModel

        init {
            itemView.container.setOnClickListener { navigationCallback.invoke(Type.TOPIC, item.id) }
            itemView.userInfoGroup.setOnClickListener { navigationCallback.invoke(Type.USER, item.user.id) }
            itemView.imageView.setOnClickListener {
                item.linked?.let { content ->
                    navigationCallback.invoke(content.linkedType, content.linkedId)
                }
            }
        }


        fun bind(item: TopicViewModel) {
            this.item = item
            setLinkedContent(item.linked)
            setUser(item.user, item.createdDate)

            with(itemView) {
                commentView.text = item.commentsCount.toString()

                if (item.event == TopicEvent.EPISODE || !item.episode.isNullOrBlank()) {
                    val tag = "${item.episode} ${context.getString(R.string.topic_tag_episode)}"
                    tagView.text = tag
                    tagView.background = ColorDrawable(context.color(R.color.topic_new_episode))
                    tagView.visible()
                } else if (item.event == TopicEvent.RELEASED) {
                    tagView.setText(R.string.topic_tag_release)
                    tagView.background = ColorDrawable(context.color(R.color.topic_release))
                    tagView.visible()
                } else {
                    tagView.text = null
                    tagView.gone()
                }


            }
        }

        private fun setUser(user: UserBrief, createdDate: String) {
            with(itemView) {
                imageLoader.setCircleImage(avatarView, user.avatar)
                nameView.text = user.nickname
                dateView.text = createdDate
            }
        }

        private fun setLinkedContent(linked: LinkedContent?) {
            with(itemView) {
                imageView.visibleIf { linked != null }


                if (linked !== null && linked is Anime) {
                    imageLoader.setImageWithPlaceHolder(imageView, linked.imageUrl)

                    val type = convertType(linked.type, linked.episodes)
                    val season = converter.convertAnimeSeasonToString(linked.dateAired)
                    val status = convertStatus(linked.status)

                    val typeText = context.getString(R.string.details_type).toBold().append(" ").append(type)
                    val seasonText = context.getString(R.string.details_season).toBold().append(" ").append(season)
                    val statusText = context.getString(R.string.details_status).toBold().append(" ").append(status)

                    typeView.text = typeText
                    seasonView.text = seasonText
                    statusView.text = statusText
                    titleView.text = linked.nameRu
                }
            }
        }

        private fun convertStatus(status: Status): String {
            return when (status) {
                Status.ANONS -> itemView.context.getString(R.string.status_anons)
                Status.ONGOING -> itemView.context.getString(R.string.status_ongoing)
                Status.RELEASED -> itemView.context.getString(R.string.status_released)
                else -> itemView.context.getString(R.string.error_no_data)
            }
        }

        private fun convertType(type: AnimeType, episodes: Int): String {
            return String.format(itemView.context.getString(R.string.type_pattern_without_duration), type.type.toUpperCase(),
                    episodes.unknownIfZero())
        }

    }
}
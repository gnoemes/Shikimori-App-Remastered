package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemTopicLinkedBinding
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Status
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicUserViewHolder
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.toBold
import com.gnoemes.shikimori.utils.unknownIfZero
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class TopicLinkedAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val converter: DateTimeConverter,
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<TopicViewModel, Any, TopicLinkedAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is TopicViewModel && item.type == TopicType.NEWS_LINK_ONLY

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemTopicLinkedBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: TopicViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemTopicLinkedBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: TopicViewModel

        private val userHolder by lazy { TopicUserViewHolder(binding.userLayout, imageLoader, navigationCallback) }

        init {
            binding.container.setOnClickListener { navigationCallback.invoke(Type.TOPIC, item.id) }
            binding.imageView.setOnClickListener {
                item.linked?.let { content ->
                    navigationCallback.invoke(content.linkedType, content.linkedId)
                }
            }
        }


        fun bind(item: TopicViewModel) {
            this.item = item
            setLinkedContent(item.linked)
            userHolder.bind(item.userData)
            binding.commentView.text = item.commentsCount.toString()
        }

        private fun setLinkedContent(linked: LinkedContent?) {
            with(binding) {
                imageView.visibleIf { linked != null }


                if (linked !== null && linked is Anime) {
                    imageLoader.setImageWithPlaceHolder(imageView, linked.imageUrl)

                    val type = convertType(linked.type, linked.episodes)
                    val season = converter.convertAnimeSeasonToString(linked.dateAired)
                    val status = convertStatus(linked.status)

                    val context = root.context
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
            val context = binding.root.context
            return when (status) {
                Status.ANONS -> context.getString(R.string.status_anons)
                Status.ONGOING -> context.getString(R.string.status_ongoing)
                Status.RELEASED -> context.getString(R.string.status_released)
                else -> context.getString(R.string.error_no_data)
            }
        }

        private fun convertType(type: AnimeType, episodes: Int): String {
            val context = binding.root.context
            return String.format(context.getString(R.string.type_pattern_without_duration), type.type.uppercase(),
                    episodes.unknownIfZero())
        }

    }
}
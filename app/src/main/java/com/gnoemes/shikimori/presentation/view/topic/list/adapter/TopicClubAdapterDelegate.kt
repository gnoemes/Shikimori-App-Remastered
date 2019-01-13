package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.domain.TopicType
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicContentViewHolder
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicUserViewHolder
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_topic_club.view.*
import kotlinx.android.synthetic.main.layout_topic.view.*

class TopicClubAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<TopicViewModel, Any, TopicClubAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean {
        return item is TopicViewModel && (item.type == TopicType.CLUB || item.type == TopicType.CLUB_PAGE || item.type == TopicType.CLUB_USER)
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_topic_club))

    override fun onBindViewHolder(item: TopicViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: TopicViewModel

        private val userHolder by lazy { TopicUserViewHolder(itemView.userLayout, imageLoader, navigationCallback) }
        private val topicHolder by lazy { TopicContentViewHolder(view.topicLayout, navigationCallback) }

        init {
            itemView.container.setOnClickListener { navigationCallback.invoke(Type.TOPIC, item.id) }
            itemView.divider.gone()
            itemView.linkedImageView.setOnClickListener {
                item.linked?.let { content ->
                    navigationCallback.invoke(content.linkedType, content.linkedId)
                }
            }

            val margin = itemView.context.dimen(R.dimen.margin_normal).toInt()
            (itemView.topicLayout.titleView.layoutParams as ConstraintLayout.LayoutParams).apply { leftMargin = margin }
        }

        fun bind(item: TopicViewModel) {
            this.item = item
            setLinkedContent(item.linked)
            userHolder.bind(item.userData)
            topicHolder.bind(item.contentData)
            itemView.commentView.text = item.commentsCount.toString()
        }

        private fun setLinkedContent(linked: LinkedContent?) {
            with(itemView) {
                linkedImageView.visibleIf { linked != null }

                if (linked !== null) {
                    imageLoader.setCircleImage(linkedImageView, linked.imageUrl)
                }
            }
        }
    }
}
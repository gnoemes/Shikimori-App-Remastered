package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicContentViewHolder
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicUserViewHolder
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_topic.view.*
import kotlinx.android.synthetic.main.layout_topic.view.*

class TopicAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<TopicViewModel, Any, TopicAdapterDelegate.ViewHolder>() {

    //fallback adapter
    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_topic))

    override fun onBindViewHolder(item: TopicViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: TopicViewModel

        private val userHolder by lazy { TopicUserViewHolder(itemView.userLayout, imageLoader, navigationCallback) }
        private val topicHolder by lazy { TopicContentViewHolder(view.topicLayout, navigationCallback) }

        private val margin = itemView.context.dimen(R.dimen.margin_normal).toInt()

        init {
            itemView.container.setOnClickListener { navigationCallback.invoke(Type.TOPIC, item.id) }
            itemView.divider.gone()
            itemView.linkedImageView.setOnClickListener {
                item.linked?.let { content ->
                    navigationCallback.invoke(content.linkedType, content.linkedId)
                }
            }

            (itemView.topicLayout.titleView.layoutParams as ConstraintLayout.LayoutParams).apply { topMargin = 0 }
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
                    imageLoader.setImageWithPlaceHolder(linkedImageView, linked.imageUrl)
                    (itemView.topicLayout.titleView.layoutParams as ConstraintLayout.LayoutParams).apply { leftMargin = margin }
                } else {
                    (itemView.topicLayout.titleView.layoutParams as ConstraintLayout.LayoutParams).apply { leftMargin = 0 }
                }
            }
        }
    }
}
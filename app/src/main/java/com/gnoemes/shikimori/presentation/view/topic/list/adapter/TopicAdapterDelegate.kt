package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.utils.color
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_topic.view.*

class TopicAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val resourceProvider: TopicResourceProvider,
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

        init {
            itemView.container.setOnClickListener { navigationCallback.invoke(Type.TOPIC, item.id) }
            itemView.userInfoGroup.setOnClickListener { navigationCallback.invoke(Type.USER, item.user.id) }
            itemView.linkedImageView.setOnClickListener {
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
                titleView.text = item.title
                contentView.linkCallback = navigationCallback
                contentView.visibleIf { !item.body.isNullOrBlank() }
                contentView.setContent(item.body)

                commentView.text = item.commentsCount.toString()

                if (item.hasTag) {
                    tagView.text = item.tag
                    tagView.background = ColorDrawable(context.color(resourceProvider.getTagColor(item.type)))
                }
                tagView.visibleIf { item.hasTag }
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
                linkedImageView.visibleIf { linked != null }

                if (linked !== null) {
                    imageLoader.setImageWithPlaceHolder(linkedImageView, linked.imageUrl)
                }
            }
        }
    }
}
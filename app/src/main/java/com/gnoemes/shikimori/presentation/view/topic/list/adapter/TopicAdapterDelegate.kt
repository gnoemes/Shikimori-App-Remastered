package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemTopicBinding
import com.gnoemes.shikimori.databinding.LayoutTopicBinding
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicContentViewHolder
import com.gnoemes.shikimori.presentation.view.topic.holders.TopicUserViewHolder
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class TopicAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val navigationCallback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<TopicViewModel, Any, TopicAdapterDelegate.ViewHolder>() {

    //fallback adapter
    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: TopicViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: TopicViewModel

        private val userHolder by lazy { TopicUserViewHolder(binding.userLayout, imageLoader, navigationCallback) }
        private val topicHolder by lazy { TopicContentViewHolder(binding.topicLayout, navigationCallback) }

        private val margin = binding.root.context.dimen(R.dimen.margin_normal).toInt()

        init {
            binding.container.setOnClickListener { navigationCallback.invoke(Type.TOPIC, item.id) }
            binding.root.findViewById<android.view.View>(R.id.divider).gone()
            binding.linkedImageView.setOnClickListener {
                item.linked?.let { content ->
                    navigationCallback.invoke(content.linkedType, content.linkedId)
                }
            }

            (binding.topicLayout.titleView.layoutParams as ConstraintLayout.LayoutParams).apply { topMargin = 0 }
        }

        fun bind(item: TopicViewModel) {
            this.item = item
            setLinkedContent(item.linked)
            userHolder.bind(item.userData)
            topicHolder.bind(item.contentData)
            binding.commentView.text = item.commentsCount.toString()
        }

        private fun setLinkedContent(linked: LinkedContent?) {
            with(binding) {
                linkedImageView.visibleIf { linked != null }

                if (linked !== null) {
                    imageLoader.setImageWithPlaceHolder(linkedImageView, linked.imageUrl)
                    (topicLayout.titleView.layoutParams as ConstraintLayout.LayoutParams).apply { leftMargin = margin }
                } else {
                    (topicLayout.titleView.layoutParams as ConstraintLayout.LayoutParams).apply { leftMargin = 0 }
                }
            }
        }
    }
}
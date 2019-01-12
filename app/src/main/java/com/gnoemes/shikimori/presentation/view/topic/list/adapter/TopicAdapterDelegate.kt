package com.gnoemes.shikimori.presentation.view.topic.list.adapter

import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.LinkedContent
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.topic.presentation.TopicViewModel
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.GlideApp
import com.gnoemes.shikimori.utils.images.ImageLoader
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

    //TODO make holder or something for toggling content view
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
            initToggle()
        }

        fun bind(item: TopicViewModel) {
            this.item = item
            setLinkedContent(item.linked)
            setUser(item.user, item.createdDate)

            with(itemView) {
                titleView.text = item.title
                contentView.linkCallback = navigationCallback
                contentView.visibleIf { !item.body.isNullOrBlank() }
                expandView.visibleIf { contentView.isVisible() }
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
                //TODO remove
                GlideApp.with(this)
                        .asDrawable()
                        .load(user.avatar)
                        .dontAnimate()
                        .into(avatarView)
//                imageLoader.setImageWithPlaceHolder(avatarView, user.avatar)
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


        //TODO replace these
        private val COLLAPSED_MAX_HEIGHT = (view.resources.displayMetrics.density * 80).toInt()
        private var contentHeight: Int = COLLAPSED_MAX_HEIGHT

        private var isExpanded = false

        private fun initToggle() {
            with(itemView) {
                contentView.post {
                    contentHeight = contentView.height
                    if (contentView.height >= COLLAPSED_MAX_HEIGHT) {
                        contentView.layoutParams.height = COLLAPSED_MAX_HEIGHT
                        contentView.requestLayout()
                        expandView.visible()
                        expandView.onClick { expandOrCollapse() }
                    } else expandView.gone()
                }
            }
        }

        private fun expandOrCollapse() {
            if (itemView.contentView.height >= COLLAPSED_MAX_HEIGHT) {
                isExpanded = !isExpanded
                if (isExpanded) itemView.expandView.setImageResource(R.drawable.ic_chevron_up)
                else itemView.expandView.setImageResource(R.drawable.ic_chevron_down)

                cycleHeightExpansion(itemView.contentView)
            }
        }

        private fun cycleHeightExpansion(layout: LinearLayout) {
            val end = if (layout.height == COLLAPSED_MAX_HEIGHT) contentHeight else COLLAPSED_MAX_HEIGHT

            ValueAnimator.ofInt(layout.height, end)
                    .apply { addUpdateListener { layout.layoutParams.apply { height = it.animatedValue as Int; layout.requestLayout() } } }
                    .setDuration(500)
                    .start()
        }

    }
}
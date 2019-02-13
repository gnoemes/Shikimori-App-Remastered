package com.gnoemes.shikimori.presentation.view.clubs.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_user_more.view.*

class UserClubAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<UserClubViewModel, Any, UserClubAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserClubViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_user_more))

    override fun onBindViewHolder(item: UserClubViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: UserClubViewModel

        init {
            itemView.container.onClick { callback.invoke(Type.CLUB, item.id) }
        }

        fun bind(item: UserClubViewModel) {
            this.item = item

            with(itemView) {
                if (item.isCensored) imageLoader.setBlurredCircleImage(avatarView, item.image.original)
                else imageLoader.setCircleImage(avatarView, item.image.original)
                nameView.text = item.name
                lastOnlineView.text = item.description
            }
        }

    }
}
package com.gnoemes.shikimori.presentation.view.clubs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemUserMoreBinding
import com.gnoemes.shikimori.entity.club.presentation.UserClubViewModel
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class UserClubAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<UserClubViewModel, Any, UserClubAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserClubViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemUserMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: UserClubViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemUserMoreBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: UserClubViewModel

        init {
            binding.container.setOnClickListener { callback.invoke(Type.CLUB, item.id) }
        }

        fun bind(item: UserClubViewModel) {
            this.item = item

            with(binding) {
                if (item.isCensored) imageLoader.setBlurredCircleImage(avatarView, item.image.original)
                else imageLoader.setCircleImage(avatarView, item.image.original)
                nameView.text = item.name
                lastOnlineView.text = item.description
            }
        }

    }
}
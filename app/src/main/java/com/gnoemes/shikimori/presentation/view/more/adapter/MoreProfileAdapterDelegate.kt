package com.gnoemes.shikimori.presentation.view.more.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemMoreProfileBinding
import com.gnoemes.shikimori.entity.more.MoreCategory
import com.gnoemes.shikimori.entity.more.MoreProfileItem
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MoreProfileAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (MoreCategory) -> Unit
) : AbsListItemAdapterDelegate<MoreProfileItem, Any, MoreProfileAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is MoreProfileItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemMoreProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: MoreProfileItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemMoreProfileBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: MoreProfileItem

        init {
            binding.container.onClick { callback.invoke(MoreCategory.PROFILE) }
        }

        fun bind(item: MoreProfileItem) {
            this.item = item

            with(binding) {
                fun onAuthorized() {
                    nameView.text = item.name
                    hintView.text = binding.root.context.getString(R.string.more_authorized_hint)
                    imageLoader.setCircleImage(imageView, item.avatar)
                }

                fun onGuest() {
                    nameView.text = binding.root.context.getString(R.string.common_guest)
                    hintView.text = binding.root.context.getString(R.string.more_guest_hint)
                }

                when (item.status) {
                    UserStatus.AUTHORIZED -> onAuthorized()
                    UserStatus.GUEST -> onGuest()
                }
            }
        }

    }
}
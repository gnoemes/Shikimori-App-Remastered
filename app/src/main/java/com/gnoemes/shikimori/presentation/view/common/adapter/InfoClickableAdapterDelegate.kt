package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemDetailsInfoClickableBinding
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.InfoClickableItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class InfoClickableAdapterDelegate(
        private val navigationCallback: (Type, Long) -> Unit,
        private val imageLoader: ImageLoader
) : AbsListItemAdapterDelegate<InfoClickableItem, Any, InfoClickableAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is InfoClickableItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemDetailsInfoClickableBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: InfoClickableItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemDetailsInfoClickableBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: InfoClickableItem

        init {
            binding.container.setOnClickListener { navigationCallback.invoke(item.type, item.id) }
        }

        fun bind(item: InfoClickableItem) {
            this.item = item
            with(binding) {
                infoView.text = item.description
                categoryView.text = item.category
                imageLoader.setCircleImage(imageView, item.image?.preview)

                imageView.visibleIf { item.image != null }
            }
        }

    }
}
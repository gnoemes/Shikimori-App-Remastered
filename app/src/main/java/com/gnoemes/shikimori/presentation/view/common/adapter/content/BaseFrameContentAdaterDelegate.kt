package com.gnoemes.shikimori.presentation.view.common.adapter.content

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemContentFrameBinding
import com.gnoemes.shikimori.entity.common.presentation.FrameItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

abstract class BaseFrameContentAdaterDelegate(
        private val imageLoader: ImageLoader
) : AbsListItemAdapterDelegate<FrameItem, Any, BaseFrameContentAdaterDelegate.FrameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): FrameHolder =
            FrameHolder(ItemContentFrameBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: FrameItem, holder: FrameHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FrameItem && isForViewType(item)

    abstract fun isForViewType(item: FrameItem): Boolean

    abstract fun onClick(item: FrameItem)

    protected open fun onClickIndexed(item: FrameItem, pos: Int) {

    }

    inner class FrameHolder(private val binding: ItemContentFrameBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: FrameItem

        init {
            binding.container.onClick { onClick(item); onClickIndexed(item, adapterPosition) }
        }

        fun bind(item: FrameItem) {
            this.item = item
            imageLoader.setImageListItem(binding.imageView, item.image.original)
            binding.nameView.text = item.name
            binding.descriptionView.text = item.typeText

            binding.nameView.visibleIf { !item.name.isNullOrEmpty() }
            binding.descriptionView.visibleIf { !item.typeText.isNullOrEmpty() }
        }
    }
}

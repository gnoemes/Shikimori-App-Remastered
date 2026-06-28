package com.gnoemes.shikimori.presentation.view.more.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemMoreCategoryBinding
import com.gnoemes.shikimori.entity.more.MoreCategory
import com.gnoemes.shikimori.entity.more.MoreCategoryItem
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MoreCategoryAdapterDelegate(
        private val callback: (MoreCategory) -> Unit
) : AbsListItemAdapterDelegate<MoreCategoryItem, Any, MoreCategoryAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is MoreCategoryItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemMoreCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: MoreCategoryItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemMoreCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: MoreCategoryItem

        init {
            binding.container.onClick { callback.invoke(item.category) }
        }

        fun bind(item: MoreCategoryItem) {
            this.item = item
            with(binding) {
                icon.setImageResource(item.icon)
                categoryView.setText(item.text)
            }
        }

    }
}
package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemLinkBinding
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class LinkAdapter(
        callback: (DetailsAction.Link) -> Unit
) : BaseAdapter<Link>() {

    init {
        delegatesManager.addDelegate(Delegate(callback))
    }

    override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean =
            oldItem == newItem

    inner class Delegate(
            private val callback: (DetailsAction.Link) -> Unit
    ) : AbsListItemAdapterDelegate<Link, Link, Delegate.ViewHolder>() {

        override fun isForViewType(item: Link, items: MutableList<Link>, position: Int): Boolean = true

        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
                ViewHolder(ItemLinkBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false))

        override fun onBindViewHolder(item: Link, holder: ViewHolder, payloads: MutableList<Any>) {
            holder.bind(item)
        }

        inner class ViewHolder(private val binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {

            private lateinit var item: Link

            init {
                binding.container.onClick { callback.invoke(DetailsAction.Link(item.url, false)) }
                binding.sharingBtn.onClick { callback.invoke(DetailsAction.Link(item.url, true)) }
            }

            fun bind(item: Link) {
                this.item = item
                binding.nameView.text = item.name
            }

        }
    }
}
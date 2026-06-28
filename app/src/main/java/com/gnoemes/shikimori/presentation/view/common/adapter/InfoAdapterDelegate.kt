package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemDetailsInfoBinding
import com.gnoemes.shikimori.entity.common.presentation.InfoItem
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class InfoAdapterDelegate : AbsListItemAdapterDelegate<InfoItem, Any, InfoAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is InfoItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemDetailsInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: InfoItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemDetailsInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InfoItem) {
            with(binding) {
                infoView.text = item.description
                categoryView.text = item.category
            }
        }
    }
}
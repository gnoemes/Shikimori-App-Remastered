package com.gnoemes.shikimori.presentation.view.userhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.databinding.ItemUserHistoryHeaderBinding
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryHeaderViewModel
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class UserHistoryHeaderAdapterDelegate : AbsListItemAdapterDelegate<UserHistoryHeaderViewModel, Any, UserHistoryHeaderAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserHistoryHeaderViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemUserHistoryHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: UserHistoryHeaderViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.binding.headerView.text = item.date
    }

    inner class ViewHolder(val binding: ItemUserHistoryHeaderBinding) : RecyclerView.ViewHolder(binding.root)
}
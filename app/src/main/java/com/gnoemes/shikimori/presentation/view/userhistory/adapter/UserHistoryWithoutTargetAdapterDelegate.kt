package com.gnoemes.shikimori.presentation.view.userhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.databinding.ItemUserHistoryWithoutTargetBinding
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryViewModel
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class UserHistoryWithoutTargetAdapterDelegate : AbsListItemAdapterDelegate<UserHistoryViewModel, Any, UserHistoryWithoutTargetAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserHistoryViewModel && item.target == null

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemUserHistoryWithoutTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: UserHistoryViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemUserHistoryWithoutTargetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserHistoryViewModel) {
            with(binding) {
                actionView.text = item.action
                dateView.text = item.actionDateString
            }
        }

    }
}
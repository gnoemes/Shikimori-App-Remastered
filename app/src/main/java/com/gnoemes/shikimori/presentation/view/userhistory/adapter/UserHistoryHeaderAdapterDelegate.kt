package com.gnoemes.shikimori.presentation.view.userhistory.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryHeaderViewModel
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_user_history_header.view.*

class UserHistoryHeaderAdapterDelegate : AbsListItemAdapterDelegate<UserHistoryHeaderViewModel, Any, UserHistoryHeaderAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserHistoryHeaderViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_user_history_header))

    override fun onBindViewHolder(item: UserHistoryHeaderViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.itemView.headerView.text = item.date
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
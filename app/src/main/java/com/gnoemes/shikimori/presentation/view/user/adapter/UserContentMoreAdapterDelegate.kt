package com.gnoemes.shikimori.presentation.view.user.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.UserContentMoreItem
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.google.android.material.card.MaterialCardView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class UserContentMoreAdapterDelegate(
        private val actionCallback: (UserProfileAction) -> Unit,
        private val layoutRes : Int
) : AbsListItemAdapterDelegate<UserContentMoreItem, Any, UserContentMoreAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserContentMoreItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(layoutRes))

    override fun onBindViewHolder(item: UserContentMoreItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val container = itemView.findViewById<MaterialCardView>(R.id.container)
        private val countView = itemView.findViewById<TextView>(R.id.moreItemsCountView)!!
        private lateinit var item : UserContentMoreItem

        init {
            container.onClick { actionCallback.invoke(UserProfileAction.More(item.type)) }
        }

        fun bind(item : UserContentMoreItem) {
            this.item = item
            val text = "+${item.size}"
            countView.text = text
        }
    }
}
package com.gnoemes.shikimori.presentation.view.forum.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import kotlinx.android.synthetic.main.item_forum.view.*

class ForumAdapter(
        private val callback: (ForumType) -> Unit
) : RecyclerView.Adapter<ForumAdapter.ViewHolder>() {

    private val items = mutableListOf<Forum>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_forum))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun bindItems(newItems: List<Forum>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: Forum

        init {
            itemView.forumNameView.onClick { callback.invoke(item.type) }
        }

        fun bind(item: Forum) {
            this.item = item
            itemView.forumNameView.text = item.name
        }

    }
}
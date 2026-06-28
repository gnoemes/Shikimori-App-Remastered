package com.gnoemes.shikimori.presentation.view.forum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemForumBinding
import com.gnoemes.shikimori.entity.forum.domain.Forum
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.utils.onClick

class ForumAdapter(
        private val callback: (ForumType) -> Unit
) : RecyclerView.Adapter<ForumAdapter.ViewHolder>() {

    private val items = mutableListOf<Forum>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemForumBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun bindItems(newItems: List<Forum>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemForumBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: Forum

        init {
            binding.forumNameView.onClick { callback.invoke(item.type) }
        }

        fun bind(item: Forum) {
            this.item = item
            binding.forumNameView.text = item.name
        }

    }
}
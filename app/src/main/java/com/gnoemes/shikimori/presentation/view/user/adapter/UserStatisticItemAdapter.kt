package com.gnoemes.shikimori.presentation.view.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.databinding.ItemUserProfileStatisticBinding
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.utils.clearAndAddAll

class UserStatisticItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<UserStatisticItem>()

    fun bindItems(newItems: List<UserStatisticItem>) {
        items.clearAndAddAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ViewHolder(ItemUserProfileStatisticBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemUserProfileStatisticBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserStatisticItem) {
            with(binding) {
                categoryView.text = item.category
                countView.text = item.count.toString()

                progressView.post {
                    progressView.layoutParams = (progressView.layoutParams as? FrameLayout.LayoutParams)?.apply {
                        val newWidth = container.measuredWidth * item.progress
                        width = if (newWidth != 0f && newWidth < container.height) container.height else newWidth.toInt()
                    }
                    progressView.invalidate()
                }
            }
        }

    }
}
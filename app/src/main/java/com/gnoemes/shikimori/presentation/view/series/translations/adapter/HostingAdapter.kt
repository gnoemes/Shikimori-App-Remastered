package com.gnoemes.shikimori.presentation.view.series.translations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.databinding.ItemChipBinding
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.utils.clearAndAddAll
import com.gnoemes.shikimori.utils.onClick

class HostingAdapter(
        private val callback: (TranslationVideo) -> Unit
) : RecyclerView.Adapter<HostingAdapter.ViewHolder>() {

    private val items = mutableListOf<TranslationVideo>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun bindItems(newItems: List<TranslationVideo>) {
        items.clearAndAddAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemChipBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: TranslationVideo

        init {
            binding.chip.onClick { callback.invoke(item) }
        }

        fun bind(item: TranslationVideo) {
            this.item = item
            binding.chip.text = item.videoHosting.synonymType
        }

    }
}
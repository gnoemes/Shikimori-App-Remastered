package com.gnoemes.shikimori.presentation.view.series.translations.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.utils.clearAndAddAll
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import kotlinx.android.synthetic.main.item_chip.view.*

class HostingAdapter(
        private val callback: (TranslationVideo) -> Unit
) : RecyclerView.Adapter<HostingAdapter.ViewHolder>() {

    private val items = mutableListOf<TranslationVideo>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_chip))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun bindItems(newItems: List<TranslationVideo>) {
        items.clearAndAddAll(newItems)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: TranslationVideo

        init {
            itemView.chip.onClick { callback.invoke(item) }
        }

        fun bind(item: TranslationVideo) {
            this.item = item
            itemView.chip.text = item.videoHosting.synonymType
        }

    }
}
package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Genre
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter(
        private val detailsCallback: (DetailsAction) -> Unit
) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private val items: MutableList<Genre> = mutableListOf()

    fun bindItems(newItems: List<Genre>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_genre))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(genre: Genre) {
            itemView.chipView.apply {
                text = genre.russianName
                onClick { detailsCallback.invoke(DetailsAction.GenreClicked(genre)) }
            }
        }
    }
}
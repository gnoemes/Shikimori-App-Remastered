package com.gnoemes.shikimori.presentation.view.rates.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.SortAction
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.rates.presentation.RateSortViewModel
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_rate_sort.view.*

class RateSortAdapterDelegate(
        private val callback: (SortAction) -> Unit
) : AbsListItemAdapterDelegate<RateSortViewModel, Any, RateSortAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is RateSortViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_rate_sort))

    override fun onBindViewHolder(item: RateSortViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: RateSortViewModel

        private var isDescending: Boolean = false
        private val iconRes: Int
            get() = if (isDescending) R.drawable.ic_sort_descending else R.drawable.ic_sort_ascending

        init {
            itemView.sortView.onClick { callback.invoke(SortAction.ChangeSort(item.sorts.map { Triple(it.first, it.second, it.first == item.currentSort) })) }
            itemView.orderView.onClick {
                isDescending = !isDescending
                callback.invoke(SortAction.ChangeOrder(isDescending))
                itemView.orderView.setImageResource(iconRes)
            }
        }

        fun bind(item: RateSortViewModel) {
            this.item = item
            this.isDescending = item.descending
            with(itemView) {
                rateStatusView.text = getLocalizedStatus(item.status, item.isAnime)
                orderView.setImageResource(iconRes)
                val sortText = item.sorts.find { it.first == item.currentSort }?.second
                sortView.text = sortText
            }
        }

        private fun getLocalizedStatus(status: RateStatus, anime: Boolean): String =
                if (anime)
                    RateStatus.values()
                            .zip(itemView.resources.getStringArray(R.array.anime_rate_stasuses))[status.ordinal]
                            .second
                else RateStatus.values()
                        .zip(itemView.resources.getStringArray(R.array.manga_rate_stasuses))[status.ordinal]
                        .second


    }
}
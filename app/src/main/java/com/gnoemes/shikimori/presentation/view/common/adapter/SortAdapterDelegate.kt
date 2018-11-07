package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.RateSort
import com.gnoemes.shikimori.entity.common.presentation.SortItem
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_sort.view.*

class SortAdapterDelegate(
        private val sortCallback: (RateSort, Boolean) -> Unit
) : AbsListItemAdapterDelegate<SortItem, Any, SortAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is SortItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_sort))

    override fun onBindViewHolder(item: SortItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var isDescending: Boolean = false
        private val iconRes: Int
            get() = if (isDescending) R.drawable.ic_sort_descending else R.drawable.ic_sort_ascending

        fun bind(item: SortItem) {
            with(itemView) {
                isDescending = item.descending
                orderView.setImageResource(iconRes)
                orderView.setOnClickListener {
                    isDescending = !isDescending
                    sortCallback.invoke(item.sorts[sortSpinnerView.selectedItemPosition].first, isDescending)
                    orderView.setImageResource(iconRes)
                }

                sortSpinnerView.apply {
                    adapter = ArrayAdapter(context, R.layout.item_sort_spinner, item.sorts.map { it.second })
                    itemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        sortCallback.invoke(item.sorts[position].first, isDescending)
                    }
                    val index = item.sorts.indexOfFirst { it.first == item.currentSort }
                    setSelection(index, false)
                }
            }
        }

    }
}
package com.gnoemes.shikimori.presentation.view.search.filter.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.utils.clearAndAddAll
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import kotlinx.android.synthetic.main.item_chip_filter.view.*

class FilterChipAdapter(
        private val type: FilterType,
        private val invertCallback: (FilterType, FilterViewModel) -> Unit,
        private val selectCallback: (FilterType, FilterViewModel) -> Unit
) : RecyclerView.Adapter<FilterChipAdapter.ViewHolder>() {

    private val items = mutableListOf<FilterViewModel>()

    fun bind(newItems: List<FilterViewModel>) {
        items.clearAndAddAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_chip_filter))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: FilterViewModel

        private val smallPadding by lazy { itemView.context.dimen(R.dimen.margin_small) }
        private val normalPadding by lazy { itemView.context.dimen(R.dimen.margin_normal) }

        init {
            itemView.chip.onClick { selectCallback.invoke(type, item) }
            itemView.chip.setOnLongClickListener { invertCallback.invoke(type, item); true }
        }

        fun bind(item: FilterViewModel) {
            this.item = item
            with(itemView) {
                chip.text = item.text
                chip.isChipIconVisible = item.state == FilterViewModel.STATE.INVERTED

                when (item.state) {
                    FilterViewModel.STATE.DEFAULT -> chip.apply { isSelected = false; isChecked = false; textStartPadding = normalPadding }
                    FilterViewModel.STATE.INVERTED -> chip.apply { isSelected = false; isChecked = true; textStartPadding = smallPadding }
                    FilterViewModel.STATE.SELECTED -> chip.apply { isSelected = true; isChecked = false; textStartPadding = normalPadding }
                }


            }
        }
    }
}
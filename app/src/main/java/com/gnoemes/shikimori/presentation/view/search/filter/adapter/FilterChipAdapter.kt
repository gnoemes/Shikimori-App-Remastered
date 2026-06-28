package com.gnoemes.shikimori.presentation.view.search.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemChipFilterBinding
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.utils.clearAndAddAll
import com.gnoemes.shikimori.utils.dimen
import com.gnoemes.shikimori.utils.onClick

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
            ViewHolder(ItemChipFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemChipFilterBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: FilterViewModel

        private val smallPadding by lazy { binding.root.context.dimen(R.dimen.margin_small) }
        private val normalPadding by lazy { binding.root.context.dimen(R.dimen.margin_normal) }

        init {
            binding.chip.onClick { selectCallback.invoke(type, item) }
            binding.chip.setOnLongClickListener { invertCallback.invoke(type, item); true }
        }

        fun bind(item: FilterViewModel) {
            this.item = item
            with(binding) {
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
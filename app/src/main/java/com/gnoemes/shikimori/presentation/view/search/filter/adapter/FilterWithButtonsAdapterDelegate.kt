package com.gnoemes.shikimori.presentation.view.search.filter.adapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemCategoryWithChipGroupAndButtonsBinding
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterAction
import com.gnoemes.shikimori.entity.search.presentation.FilterViewModel
import com.gnoemes.shikimori.entity.search.presentation.FilterWithButtonsViewModel
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.google.android.flexbox.FlexboxLayoutManager
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FilterWithButtonsAdapterDelegate(
        private val invertCallback: (FilterType, FilterViewModel) -> Unit,
        private val selectCallback: (FilterType, FilterViewModel) -> Unit,
        private val actionCallback: (FilterType, FilterAction) -> Unit
) : AbsListItemAdapterDelegate<FilterWithButtonsViewModel, Any, FilterWithButtonsAdapterDelegate.ViewHolder>() {

    private val sharedPool = RecyclerView.RecycledViewPool()

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterWithButtonsViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemCategoryWithChipGroupAndButtonsBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: FilterWithButtonsViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemCategoryWithChipGroupAndButtonsBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: FilterWithButtonsViewModel

        private val bigMargin by lazy { binding.root.context.dp(26) }
        private val defaultMargin by lazy { binding.root.context.dp(16) }

        init {
            with(binding) {
                chipList.setRecycledViewPool(sharedPool)
                chipList.itemAnimator = null
                clearBtn.onClick { actionCallback.invoke(item.type, FilterAction.Clear) }
                invertBtn.onClick { actionCallback.invoke(item.type, FilterAction.Invert) }
                checkAllBtn.onClick { actionCallback.invoke(item.type, FilterAction.SelectAll) }
            }
        }

        fun bind(item: FilterWithButtonsViewModel) {
            this.item = item
            with(binding) {
                categoryNameView.text = item.categoryLocalised

                val chipAdapter = FilterChipAdapter(item.type, invertCallback, selectCallback).apply { if (!hasObservers()) setHasStableIds(true) }

                with(chipList) {
                    adapter = chipAdapter
                    layoutManager = FlexboxLayoutManager(context)
                }

                chipAdapter.bind(item.filters)

                val filtersApplied = item.filters.find { it.state != FilterViewModel.STATE.DEFAULT } != null

                clearBtn.visibleIf { item.hasDelete && filtersApplied }
                invertBtn.visibleIf { item.hasInvert && filtersApplied }
                checkAllBtn.visibleIf { item.hasSelectAll }

                val margin = if (item.hasDelete || item.hasSelectAll || item.hasInvert) bigMargin
                else defaultMargin

                dynamicSpace.layoutParams = (dynamicSpace.layoutParams as ConstraintLayout.LayoutParams).apply { height = margin }
            }
        }

    }
}
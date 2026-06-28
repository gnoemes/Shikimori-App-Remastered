package com.gnoemes.shikimori.presentation.view.search.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemCategoryWithButtonBinding
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterAction
import com.gnoemes.shikimori.entity.search.presentation.FilterNestedViewModel
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FilterNestedAdapterDelegate(
        private val actionCallback: (FilterType, FilterAction) -> Unit
) : AbsListItemAdapterDelegate<FilterNestedViewModel, Any, FilterNestedAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterNestedViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemCategoryWithButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: FilterNestedViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemCategoryWithButtonBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: FilterNestedViewModel

        init {
            binding.countBtn.onClick { actionCallback.invoke(item.type, FilterAction.ShowNested) }
            binding.container.onClick { actionCallback.invoke(item.type, FilterAction.ShowNested) }
            binding.clearBtn.onClick { actionCallback.invoke(item.type, FilterAction.Clear) }
        }

        fun bind(item: FilterNestedViewModel) {
            this.item = item
            with(binding) {
                val countText = if (item.appliedCount > 0) "${item.appliedCount}" else "+"
                countBtn.text = countText
                categoryName.text = item.categoryLocalised

                countBtn.isSelected = item.appliedCount > 0
                clearBtn.visibleIf { item.appliedCount > 0 }
            }
        }

    }
}
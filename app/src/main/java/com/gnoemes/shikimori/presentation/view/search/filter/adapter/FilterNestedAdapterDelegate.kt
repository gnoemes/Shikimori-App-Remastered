package com.gnoemes.shikimori.presentation.view.search.filter.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.entity.search.presentation.FilterAction
import com.gnoemes.shikimori.entity.search.presentation.FilterNestedViewModel
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_category_with_button.view.*

class FilterNestedAdapterDelegate(
        private val actionCallback: (FilterType, FilterAction) -> Unit
) : AbsListItemAdapterDelegate<FilterNestedViewModel, Any, FilterNestedAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterNestedViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_category_with_button))

    override fun onBindViewHolder(item: FilterNestedViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: FilterNestedViewModel

        init {
            itemView.countBtn.onClick { actionCallback.invoke(item.type, FilterAction.ShowNested) }
            itemView.container.onClick { actionCallback.invoke(item.type, FilterAction.ShowNested) }
            itemView.clearBtn.onClick { actionCallback.invoke(item.type, FilterAction.Clear) }
        }

        fun bind(item: FilterNestedViewModel) {
            this.item = item
            with(itemView) {
                countBtn.text = "${item.appliedCount}"
                categoryName.text = item.categoryLocalised

                countBtn.isSelected = item.appliedCount > 0
                clearBtn.visibleIf { item.appliedCount > 0 }
            }
        }

    }
}
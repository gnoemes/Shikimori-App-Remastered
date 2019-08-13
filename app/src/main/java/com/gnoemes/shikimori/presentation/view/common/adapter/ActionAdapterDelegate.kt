package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsActionType
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_details_action.view.*

class ActionAdapterDelegate(
        private val callback: (DetailsAction) -> Unit
) : AbsListItemAdapterDelegate<DetailsActionType, Any, ActionAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is DetailsActionType

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_details_action))

    override fun onBindViewHolder(item: DetailsActionType, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: DetailsActionType

        init {
            itemView.container.onClick { callback.invoke(getClickAction(item)) }
        }

        fun bind(item: DetailsActionType) {
            this.item = item
            with(itemView) {
                val icon = when (item) {
                    DetailsActionType.CHRONOLOGY -> R.drawable.ic_chronology
                    DetailsActionType.DISCUSSION -> R.drawable.ic_comment
                    DetailsActionType.LINKS -> R.drawable.ic_links
                    DetailsActionType.SIMILAR -> R.drawable.ic_similar
                    DetailsActionType.STATISTIC -> R.drawable.ic_stats
                }

                val text =  when (item) {
                    DetailsActionType.CHRONOLOGY -> R.string.common_chronology
                    DetailsActionType.DISCUSSION -> R.string.common_discussion
                    DetailsActionType.LINKS -> R.string.common_links
                    DetailsActionType.SIMILAR -> R.string.common_similar
                    DetailsActionType.STATISTIC -> R.string.common_statistic
                }

                imageView.setImageResource(icon)
                categoryView.setText(text)
            }
        }

        private fun getClickAction(type: DetailsActionType): DetailsAction = when (type) {
            DetailsActionType.CHRONOLOGY -> DetailsAction.Chronology
            DetailsActionType.DISCUSSION -> DetailsAction.Discussion
            DetailsActionType.LINKS -> DetailsAction.Links
            DetailsActionType.SIMILAR -> DetailsAction.Similar
            DetailsActionType.STATISTIC -> DetailsAction.Statistic
        }

    }
}
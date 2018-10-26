package com.gnoemes.shikimori.presentation.view.person.adapter

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.roles.presentation.PersonDescriptionItem
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_details_description.view.*

class PersonDescriptionAdapterDelegate : AbsListItemAdapterDelegate<PersonDescriptionItem, Any, PersonDescriptionAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is PersonDescriptionItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_details_description))

    override fun onBindViewHolder(item: PersonDescriptionItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var isExpanded: Boolean = false

        fun bind(item: PersonDescriptionItem) {
            with(itemView) {
                descriptionLabelView.setText(R.string.common_roles)
                descriptionTextView.text = item.roles
                expandView.onClick {
                    isExpanded = !isExpanded
                    if (isExpanded) itemView.expandView.setImageResource(R.drawable.ic_chevron_up)
                    else itemView.expandView.setImageResource(R.drawable.ic_chevron_down)

                    cycleTextViewExpansion(descriptionTextView)
                }


                descriptionTextView.post {
                    expandView.visibleIf { descriptionTextView.lineCount > 4 }
                }
            }
        }

        private fun cycleTextViewExpansion(tv: TextView) {
            val collapsedMaxLines = 4
            val duration = (tv.lineCount - collapsedMaxLines) * 10L
            val animation = ObjectAnimator.ofInt(tv, "maxLines",
                    if (tv.maxLines == collapsedMaxLines) tv.lineCount else collapsedMaxLines)
            animation.setDuration(duration).start()


        }
    }
}
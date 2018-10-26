package com.gnoemes.shikimori.presentation.view.person.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.roles.presentation.PersonHeadItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_person_head.view.*

class PersonHeadAdapterDelegate(
        private val imageLoader: ImageLoader
) : AbsListItemAdapterDelegate<PersonHeadItem, Any, PersonHeadAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is PersonHeadItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_person_head))

    override fun onBindViewHolder(item: PersonHeadItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PersonHeadItem) {
            with(itemView) {
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)

                onEngView.text = item.name

                onJpLabelView.visibleIf { !item.nameJp.isNullOrBlank() }
                onJpView.visibleIf { !item.nameJp.isNullOrBlank() }
                onJpView.text = item.nameJp

                birthdayLabelView.visibleIf { !item.birthDay.isNullOrBlank() }
                birthdayView.visibleIf { !item.birthDay.isNullOrBlank() }
                birthdayView.text = item.birthDay

                jobView.visibleIf { !item.jobTitle.isNullOrBlank() }
                jobView.text = item.jobTitle
            }
        }

    }
}
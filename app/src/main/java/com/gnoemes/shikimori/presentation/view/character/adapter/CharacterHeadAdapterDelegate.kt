package com.gnoemes.shikimori.presentation.view.character.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.roles.presentation.CharacterHeadItem
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.visibleIf
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_character_head.view.*

class CharacterHeadAdapterDelegate(
        private val imageLoader: ImageLoader
) : AbsListItemAdapterDelegate<CharacterHeadItem, Any, CharacterHeadAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is CharacterHeadItem

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_character_head))

    override fun onBindViewHolder(item: CharacterHeadItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: CharacterHeadItem) {
            with(itemView) {
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)

                onRuLabelView.visibleIf { !item.nameRu.isNullOrBlank() }
                onRuView.visibleIf { !item.nameRu.isNullOrBlank() }
                onRuView.text = item.nameRu

                onJpLabelView.visibleIf { !item.nameJp.isNullOrBlank() }
                onJpView.visibleIf { !item.nameJp.isNullOrBlank() }
                onJpView.text = item.nameJp

                otherLabelView.visibleIf { !item.nameAlt.isNullOrBlank() }
                otherView.visibleIf { !item.nameAlt.isNullOrBlank() }
                otherView.text = item.nameAlt
            }
        }

    }
}
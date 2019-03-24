package com.gnoemes.shikimori.presentation.view.series.translations.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.domain.TranslationMenu
import com.gnoemes.shikimori.entity.series.presentation.TranslationVideo
import com.gnoemes.shikimori.entity.series.presentation.TranslationViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.StartSnapHelper
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visibleIf
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_translation.view.*

class TranslationAdapterDelegate(
        private val callback: (TranslationVideo) -> Unit,
        private val menuListener: (TranslationMenu) -> Unit
) : AbsListItemAdapterDelegate<TranslationViewModel, Any, TranslationAdapterDelegate.ViewHolder>() {


    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is TranslationViewModel

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_translation))

    override fun onBindViewHolder(item: TranslationViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val snapOffset = itemView.resources.getDimension(R.dimen.margin_big).toInt()
        private lateinit var item: TranslationViewModel
        private val layoutManager by lazy { LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false) }

        private val videoHostingCallback = { videoData: TranslationVideo -> callback.invoke(videoData) }
        private val adapter by lazy { HostingAdapter(videoHostingCallback) }

        init {
            if (itemView.hostingRecyclerView.onFlingListener == null) {
                val snapHelper = StartSnapHelper(snapOffset)
                snapHelper.attachToRecyclerView(itemView.hostingRecyclerView)
            }

            itemView.hostingRecyclerView.apply {
                layoutManager = this@ViewHolder.layoutManager
                adapter = this@ViewHolder.adapter
                addItemDecoration(HorizontalSpaceItemDecorator(resources.getDimension(R.dimen.margin_normal).toInt(), snapOffset))
            }

            itemView.apply {
                authorView.onClick { menuListener.invoke(TranslationMenu.Author(item.authors)) }
                menuView.onClick { menuListener.invoke(TranslationMenu.Download(item.videos)) }
            }
        }

        fun bind(item: TranslationViewModel) {
            this.item = item
            with(itemView) {
                authorView.text = item.authors
                descriptionView.text = item.description
                descriptionView.visibleIf { !item.description.isNullOrBlank() }
                sameAuthorView.visibleIf { item.isSameAuthor }
                menuView.visibleIf { item.canBeDownloaded }
                adapter.bindItems(item.videos)
            }
        }
    }

}
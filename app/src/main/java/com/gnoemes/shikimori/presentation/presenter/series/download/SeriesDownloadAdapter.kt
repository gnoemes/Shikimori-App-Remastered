package com.gnoemes.shikimori.presentation.presenter.series.download

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.presentation.SeriesDownloadItem
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import kotlinx.android.synthetic.main.item_series_download.view.*

class SeriesDownloadAdapter(
        private val items: List<SeriesDownloadItem>,
        private val callback: SeriesDownloadDialog.SeriesDownloadCallback?,
        private val onAction: () -> Unit
) : RecyclerView.Adapter<SeriesDownloadAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_series_download))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: SeriesDownloadItem

        init {
            itemView.downloadBtn.onClick { callback?.onDownload(item.url, item.video); onAction.invoke() }
            itemView.sharingBtn.onClick { callback?.onShare(item.url) }
        }

        fun bind(item: SeriesDownloadItem) {
            this.item = item
            with(itemView) {
                hostingView.text = item.hosting
                qualityView.text = item.quality
            }
        }

    }
}
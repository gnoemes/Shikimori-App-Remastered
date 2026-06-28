package com.gnoemes.shikimori.presentation.presenter.series.download

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.databinding.ItemSeriesDownloadBinding
import com.gnoemes.shikimori.entity.series.presentation.SeriesDownloadItem
import com.gnoemes.shikimori.utils.onClick

class SeriesDownloadAdapter(
        private val items: List<SeriesDownloadItem>,
        private val callback: SeriesDownloadDialog.SeriesDownloadCallback?,
        private val onAction: () -> Unit
) : RecyclerView.Adapter<SeriesDownloadAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(ItemSeriesDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemSeriesDownloadBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: SeriesDownloadItem

        init {
            binding.episodeContainer.onClick { callback?.onDownload(item.url, item.video); onAction.invoke() }
            binding.sharingBtn.onClick { callback?.onShare(item.url) }
        }

        fun bind(item: SeriesDownloadItem) {
            this.item = item
            binding.hostingView.text = item.hosting
            binding.qualityView.text = item.quality
        }

    }
}

package com.gnoemes.shikimori.presentation.presenter.series.download

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.SeriesDownloadItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.dimenAttr
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.dialog_base_bottom_sheet.*
import kotlinx.android.synthetic.main.dialog_series_download.*

class SeriesDownloadDialog : BaseBottomSheetDialogFragment() {

    companion object {
        fun newInstance(title: String, items: List<SeriesDownloadItem>) = SeriesDownloadDialog().withArgs {
            putString(TITLE_KEY, title)
            putParcelableArray(ITEMS_KEY, items.toTypedArray())
        }

        private const val TITLE_KEY = "TITLE_KEY"
        private const val ITEMS_KEY = "ITEMS_KEY"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = context.dimenAttr(android.R.attr.actionBarSize)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = arguments?.getParcelableArray(ITEMS_KEY)
                ?.map { it as SeriesDownloadItem }
                ?.toList() ?: emptyList()

        with(toolbar) {
            title = arguments?.getString(TITLE_KEY)
        }

        val seriesAdapter = SeriesDownloadAdapter(items, (parentFragment as? SeriesDownloadCallback)) { dismiss() }

        with(recyclerView) {
            adapter = seriesAdapter
            layoutManager = LinearLayoutManager(context)
            val margin = context.dp(16)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(10), true, margin, margin))
        }

    }

    override fun getDialogLayout(): Int = R.layout.dialog_series_download

    interface SeriesDownloadCallback {
        fun onDownload(url: String, video: Video)
        fun onShare(url: String)
    }
}
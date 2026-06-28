package com.gnoemes.shikimori.presentation.presenter.series.download

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.DialogSeriesDownloadBinding
import com.gnoemes.shikimori.entity.series.domain.Video
import com.gnoemes.shikimori.entity.series.presentation.SeriesDownloadItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.utils.dimenAttr
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs

class SeriesDownloadDialog : BaseBottomSheetDialogFragment() {

    private var _binding: DialogSeriesDownloadBinding? = null
    private val binding: DialogSeriesDownloadBinding? get() = _binding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogSeriesDownloadBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = _binding ?: return

        @Suppress("DEPRECATION")
        val items = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArray(ITEMS_KEY, SeriesDownloadItem::class.java)
                    ?.map { it as SeriesDownloadItem }
                    ?.toList() ?: emptyList()
        } else {
            arguments?.getParcelableArray(ITEMS_KEY)
                    ?.map { it as SeriesDownloadItem }
                    ?.toList() ?: emptyList()
        }

        val seriesAdapter = SeriesDownloadAdapter(items, (parentFragment as? SeriesDownloadCallback)) { dismiss() }

        with(b.recyclerView) {
            adapter = seriesAdapter
            layoutManager = LinearLayoutManager(context)
            val margin = context.dp(16)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(10), true, margin, margin))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getDialogLayout(): Int = R.layout.dialog_series_download

    interface SeriesDownloadCallback {
        fun onDownload(url: String, video: Video)
        fun onShare(url: String)
    }
}

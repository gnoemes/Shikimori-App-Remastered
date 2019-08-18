package com.gnoemes.shikimori.presentation.view.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.presentation.view.common.adapter.LinkAdapter
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.fragment_links.*

class LinkDialogFragment : BaseBottomSheetDialogFragment() {

    companion object {
        fun newInstance(links: List<Link>) = LinkDialogFragment().withArgs { putParcelableArray(LINKS_KEY, links.toTypedArray()) }
        private const val LINKS_KEY = "LINKS_KEY"
    }

    private val adapter by lazy { LinkAdapter(this::onClick) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getDialogLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton(R.drawable.ic_close) { dismiss() }
            setTitle(R.string.common_links)
        }

        with(recyclerView) {
            adapter = this@LinkDialogFragment.adapter
            layoutManager = LinearLayoutManager(context)
            val margin = dp(16)
            addItemDecoration(VerticalSpaceItemDecorator(dp(10), true, margin, margin))
        }

        val items = (arguments?.getParcelableArray(LINKS_KEY) ?: emptyArray()).filterIsInstance(Link::class.java)
        adapter.bindItems(items)
    }

    private fun onClick(action: DetailsAction.Link) {
        (parentFragment as? LinkCallback)?.onLinkAction(action)
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.fragment_links

    interface LinkCallback {
        fun onLinkAction(action: DetailsAction.Link)
    }
}
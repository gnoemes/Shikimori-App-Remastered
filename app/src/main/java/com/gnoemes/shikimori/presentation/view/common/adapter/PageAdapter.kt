package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import com.gnoemes.shikimori.utils.widgets.ViewStatePagerAdapter

class PageAdapter(
        private val pages: List<View>
) : ViewStatePagerAdapter() {

    override fun createView(container: ViewGroup?, position: Int): View {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.count()
    }

}
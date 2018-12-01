package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import com.gnoemes.shikimori.utils.widgets.ViewStatePagerAdapter

class PageTitleAdapter(
        private val pages: List<Pair<String, View>>
) : ViewStatePagerAdapter() {

    override fun createView(container: ViewGroup?, position: Int): View {
        return pages[position].second
    }

    override fun getCount(): Int {
        return pages.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].first
    }
}
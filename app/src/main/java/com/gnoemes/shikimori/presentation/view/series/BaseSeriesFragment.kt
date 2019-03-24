package com.gnoemes.shikimori.presentation.view.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Image
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_base_series.*
import kotlinx.android.synthetic.main.layout_toolbar_transparent.*
import javax.inject.Inject

abstract class BaseSeriesFragment<Presenter : BaseNetworkPresenter<View>, View : BaseSeriesView> : BaseFragment<Presenter, View>(), BaseSeriesView {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val view = inflater.inflate(R.layout.fragment_base_series, container, false)
        if (getFragmentLayout() != android.view.View.NO_ID) {
            inflater.inflate(getFragmentLayout(), view.findViewById(R.id.seriesFragmentContainer), true)
        }
        return view
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            setTitle(R.string.series_show)
            (layoutParams as AppBarLayout.LayoutParams).apply { scrollFlags = 0 }
            addBackButton { getPresenter().onBackPressed() }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setBackground(image: Image) {
        imageLoader.setBlurredImage(backgroundImage, image.original, sampling = 2)
    }

    override fun setName(name: String) {
        titleView?.text = name
    }

}
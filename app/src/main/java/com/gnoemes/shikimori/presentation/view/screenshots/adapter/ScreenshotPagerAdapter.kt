package com.gnoemes.shikimori.presentation.view.screenshots.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.viewpager.widget.PagerAdapter
import com.github.ybq.android.spinkit.SpinKitView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.anime.domain.Screenshot
import com.gnoemes.shikimori.presentation.view.common.widget.TouchImageView
import com.gnoemes.shikimori.utils.images.GlideApp
import com.gnoemes.shikimori.utils.images.TouchImageTargetView

class ScreenshotPagerAdapter(
        val items: List<Screenshot>,
        private val uiVisibilityCallback: () -> Unit,
        private val swipeCallback: () -> Unit,
        private val dismissCallback: () -> Unit
) : PagerAdapter() {

    override fun getCount(): Int = items.size

    @SuppressLint("ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_screenshot_page, null)
        val photoView = view.findViewById<TouchImageView>(R.id.imageView)
        val progressBar = view.findViewById<SpinKitView>(R.id.progressBar)

        val item = items[position]
        GlideApp.with(photoView)
                .asBitmap()
                .load(item.original)
                .into(TouchImageTargetView(photoView, progressBar))

        photoView.setOnDoubleTapListener(tapListener)
        photoView.setOnTouchImageViewListener(swipeListener)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    private val swipeListener = object : TouchImageView.OnTouchImageViewListener {
        override fun onMove() = Unit
        override fun onDismiss() = dismissCallback.invoke()
        override fun onSwipe() = swipeCallback.invoke()
    }

    private val tapListener = object : GestureDetector.OnDoubleTapListener {
        override fun onDoubleTap(e: MotionEvent?): Boolean = false
        override fun onDoubleTapEvent(e: MotionEvent?): Boolean = false
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            uiVisibilityCallback.invoke()
            return true
        }
    }

}
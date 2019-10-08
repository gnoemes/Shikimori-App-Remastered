package com.gnoemes.shikimori.utils.images

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ProgressBar
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.visible
import com.gnoemes.shikimori.presentation.view.common.widget.TouchImageView

class TouchImageTargetView(
        view: TouchImageView,
        private val progress: ProgressBar? = null
) : CustomViewTarget<TouchImageView, Bitmap>(view) {

    override fun onLoadFailed(errorDrawable: Drawable?) {
        view.setImageResource(R.drawable.missing_original)
    }

    override fun onResourceCleared(placeholder: Drawable?) {
    }

    override fun onResourceLoading(placeholder: Drawable?) {
        progress?.visible()
    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        progress?.gone()
        view.setImageBitmap(resource)
    }
}
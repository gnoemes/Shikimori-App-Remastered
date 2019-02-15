package com.gnoemes.shikimori.utils.images

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.utils.images.blur.BlurTransformation
import javax.inject.Inject

class GlideImageLoader @Inject constructor(
        private val context: Context
) : ImageLoader {

    val glide = GlideApp.with(context)

    override fun setCircleImage(image: ImageView, url: String?) {
        GlideApp.with(image)
                .asDrawable()
                .load(url)
                .dontAnimate()
                .into(image)
    }

    override fun setImageWithPlaceHolder(image: ImageView, url: String?) {
        GlideApp.with(image)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.NORMAL)
                .error(R.drawable.missing_original)
                .centerCrop()
                .into(image)
    }

    override fun setImageListItem(image: ImageView, url: String?) {
        GlideApp.with(image)
                .asBitmap()
                .dontAnimate()
                .error(R.drawable.missing_original)
                .centerCrop()
                .load(url)
                .override(image.measuredWidth / 2, image.measuredHeight / 2)
                .into(BitmapImageViewTarget(image).apply { waitForLayout() })
    }

    override fun setBlurredImage(image: ImageView, url: String?, radius : Int, sampling : Int) {
        GlideApp.with(image)
                .asBitmap()
                .load(url)
                .transform(BlurTransformation(radius, sampling))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .priority(Priority.HIGH)
                .dontAnimate()
                .into(image)
    }

    override fun setBlurredCircleImage(image: ImageView, url: String?, radius: Int, sampling: Int) {
        GlideApp.with(image)
                .asDrawable()
                .load(url)
                .transform(BlurTransformation(radius, sampling))
                .dontAnimate()
                .into(image)
    }

    override fun clearImage(image: ImageView) {
        glide.clear(image)
    }
}
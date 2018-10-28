package com.gnoemes.shikimori.utils.images

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.gnoemes.shikimori.R
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import javax.inject.Inject

class GlideImageLoader @Inject constructor(
        private val context: Context
) : ImageLoader {

    val glide = GlideApp.with(context)

    override fun setImageWithPlaceHolder(image: ImageView, url: String?) {
        GlideApp.with(image)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(true)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .priority(Priority.NORMAL)
                .error(R.drawable.missing_original)
                .centerCrop()
                .placeholder(android.R.color.transparent)
                .thumbnail(0.25f)
                .into(image)
    }

    override fun setImageListItem(image: ImageView, url: String?) {
        GlideApp.with(image)
                .asBitmap()
                .dontAnimate()
                .skipMemoryCache(true)
                .error(R.drawable.missing_original)
                .centerCrop()
                .load(url)
                .override(image.measuredWidth / 2, image.measuredHeight / 2)
                .into(BitmapImageViewTarget(image).apply { waitForLayout() })
    }

    override fun clearImage(image: ImageView) {
        glide.clear(image)
    }
}
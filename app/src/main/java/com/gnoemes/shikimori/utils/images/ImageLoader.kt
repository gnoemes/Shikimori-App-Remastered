package com.gnoemes.shikimori.utils.images

import android.widget.ImageView

interface ImageLoader {

    fun setImageWithPlaceHolder(image: ImageView, url: String?)

    fun clearImage(image: ImageView)
}
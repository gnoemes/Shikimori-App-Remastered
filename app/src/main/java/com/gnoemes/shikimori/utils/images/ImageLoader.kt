package com.gnoemes.shikimori.utils.images

import android.widget.ImageView

interface ImageLoader {

    fun setCircleImage(image : ImageView, url : String?)

    fun setImageWithPlaceHolder(image: ImageView, url: String?)

    fun setImageListItem(image: ImageView, url: String?)

    fun clearImage(image: ImageView)
}
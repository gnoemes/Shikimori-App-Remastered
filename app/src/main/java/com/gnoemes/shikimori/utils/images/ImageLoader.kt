package com.gnoemes.shikimori.utils.images

import android.widget.ImageView

interface ImageLoader {

    fun setCircleImage(image : ImageView, url : String?)

    fun setImageWithPlaceHolder(image: ImageView, url: String?)

    fun setImageListItem(image: ImageView, url: String?)

    fun setBlurredImage(image : ImageView, url : String?,  radius : Int = 25, sampling : Int = 1)

    fun setBlurredCircleImage(image : ImageView, url : String?, radius : Int = 25, sampling : Int = 1)

    fun clearImage(image: ImageView)
}
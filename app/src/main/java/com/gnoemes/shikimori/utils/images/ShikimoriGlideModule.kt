package com.gnoemes.shikimori.utils.images

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.io.InputStream

@GlideModule
class ShikimoriGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        with(builder) {
            setDiskCache(InternalCacheDiskCacheFactory(context, (50L * 1024 * 1024)))
            setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565))
            setDefaultTransitionOptions(Bitmap::class.java, BitmapTransitionOptions.withCrossFade())
            setLogLevel(Log.DEBUG)
            setBitmapPool(LruBitmapPool((5L * 1024 * 1024)))
            setMemoryCache(LruResourceCache(5L * 1024 * 1024))
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder().cache(Cache(File(context.cacheDir, "net_cache"), 5L * 1024 * 1024)).build()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }
}
package com.gnoemes.shikimori.di.series

import android.content.Context
import com.gnoemes.shikimori.data.network.*
import com.gnoemes.shikimori.data.network.impl.ShimoriAnimeSourceImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class SeriesAnimeModule {

    @Provides
    @Reusable
    fun provideAnimeSource(context: Context, videoApi: VideoApi, shimoriVideoApi : ShimoriVideoApi, shikicinemaVideoApi: ShikicinemaVideoApi, animeApi: AnimeApi) : AnimeSource {
      return  ShimoriAnimeSourceImpl(videoApi, shimoriVideoApi, shikicinemaVideoApi, animeApi)
    }
}
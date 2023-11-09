package com.gnoemes.shikimori.di.series

import android.content.Context
import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.ShimoriVideoApi
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.data.network.impl.ShimoriAnimeSourceImpl
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class SeriesAnimeModule {

    @Provides
    @Reusable
    fun provideAnimeSource(context: Context, videoApi: VideoApi, shimoriVideoApi : ShimoriVideoApi) : AnimeSource {
      return  ShimoriAnimeSourceImpl(videoApi, shimoriVideoApi)
    }
}
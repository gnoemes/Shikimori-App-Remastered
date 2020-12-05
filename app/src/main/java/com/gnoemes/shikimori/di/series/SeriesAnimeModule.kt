package com.gnoemes.shikimori.di.series

import android.content.Context
import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.PlashikiVideoApi
import com.gnoemes.shikimori.data.network.VideoApi
import com.gnoemes.shikimori.data.network.impl.CloudAnimeSourceImpl
import com.gnoemes.shikimori.data.network.impl.PlashikiAnimeSourceImpl
import com.gnoemes.shikimori.entity.app.domain.SettingsExtras
import com.gnoemes.shikimori.utils.getDefaultSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class SeriesAnimeModule {

    @Provides
    @Reusable
    fun provideAnimeSource(context: Context, videoApi: VideoApi, plashikiVideoApi : PlashikiVideoApi) : AnimeSource {
        val usePlashikiApi = context.getDefaultSharedPreferences().getBoolean(SettingsExtras.USE_PLASHIKI_API, false)
        return if (usePlashikiApi) PlashikiAnimeSourceImpl(videoApi, plashikiVideoApi)
        else CloudAnimeSourceImpl(videoApi)
    }
}
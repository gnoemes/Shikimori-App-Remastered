package com.gnoemes.shikimori.di.series

import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface EpisodeRepositoryModule {

    @Binds
    @Reusable
    fun bindSeriesRepository(seriesRepository: SeriesRepositoryImpl): SeriesRepository

}
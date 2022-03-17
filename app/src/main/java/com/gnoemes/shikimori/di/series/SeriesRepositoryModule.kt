package com.gnoemes.shikimori.di.series

import com.gnoemes.shikimori.data.repository.series.shikimori.DynamicAgentRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.DynamicAgentRepositoryImpl
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module(includes = [SeriesAnimeModule::class])
interface SeriesRepositoryModule {

    @Binds
    @Reusable
    fun bindSeriesRepository(seriesRepository: SeriesRepositoryImpl): SeriesRepository

    @Binds
    @Reusable
    fun bindDynamicAgentRepository(repository: DynamicAgentRepositoryImpl): DynamicAgentRepository

}
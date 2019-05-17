package com.gnoemes.shikimori.di.series

import com.gnoemes.shikimori.data.network.AnimeSource
import com.gnoemes.shikimori.data.network.impl.CloudAnimeSourceImpl
import com.gnoemes.shikimori.data.repository.series.shikimori.DynamicAgentRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.DynamicAgentRepositoryImpl
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepository
import com.gnoemes.shikimori.data.repository.series.shikimori.SeriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface SeriesRepositoryModule {

    @Binds
    @Reusable
    fun bindSeriesRepository(seriesRepository: SeriesRepositoryImpl): SeriesRepository

    //Todo make dynamic switch?
    @Binds
    @Reusable
    fun bindAnimeSource(cloudSource: CloudAnimeSourceImpl): AnimeSource

//    @Binds
//    @Reusable
//    fun bindAnimeSource(cloudSource: LocalAnimeSourceImpl): AnimeSource

    @Binds
    @Reusable
    fun bindDynamicAgentRepository(repository: DynamicAgentRepositoryImpl): DynamicAgentRepository

}
package com.gnoemes.shikimori.di.series

import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractorImpl
import com.gnoemes.shikimori.domain.series.SeriesSyncInteractor
import com.gnoemes.shikimori.domain.series.SeriesSyncInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface SeriesInteractorModule {
    @Binds
    @Reusable
    fun bindSeriesInteractor(interactor: SeriesInteractorImpl): SeriesInteractor

    @Binds
    @Reusable
    fun bindSeriesSyncInteractor(interactor: SeriesSyncInteractorImpl): SeriesSyncInteractor
}
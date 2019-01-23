package com.gnoemes.shikimori.di.series

import com.gnoemes.shikimori.domain.series.SeriesInteractor
import com.gnoemes.shikimori.domain.series.SeriesInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface SeriesInteractorModule {
    @Binds
    @Reusable
    abstract fun bindSeriesInteractor(interactor: SeriesInteractorImpl): SeriesInteractor

}
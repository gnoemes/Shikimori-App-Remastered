package com.gnoemes.shikimori.di.series

import com.gnoemes.shikimori.di.rate.RateInteractorModule
import com.gnoemes.shikimori.di.rate.RateUtilModule
import com.gnoemes.shikimori.di.rate.SyncModule
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import dagger.Module


@Module(includes = [
    RateUtilModule::class,
    RateInteractorModule::class,
    SyncModule::class,
    UserUtilModule::class,
    SeriesRepositoryModule::class,
    SeriesInteractorModule::class,
    SeriesUtilModule::class,
    UserInteractorModule::class
])
interface SeriesSyncServiceModule
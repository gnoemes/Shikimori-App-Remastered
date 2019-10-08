package com.gnoemes.shikimori.di.rate

import com.gnoemes.shikimori.data.local.db.PinnedRateDbSource
import com.gnoemes.shikimori.data.local.db.impl.PinnedRateDbSourceImpl
import com.gnoemes.shikimori.data.repository.rates.PinnedRateRepository
import com.gnoemes.shikimori.data.repository.rates.PinnedRateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module(includes = [AnimeProgressModule::class])
interface SyncModule {

    @Binds
    @Reusable
    fun bindPinnedSource(source : PinnedRateDbSourceImpl) : PinnedRateDbSource

    @Binds
    @Reusable
    fun bindPinnedRepo(repo : PinnedRateRepositoryImpl) : PinnedRateRepository

}
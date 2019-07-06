package com.gnoemes.shikimori.di.rate

import com.gnoemes.shikimori.data.local.db.*
import com.gnoemes.shikimori.data.local.db.impl.*
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.data.repository.common.impl.RateResponseConverterImpl
import com.gnoemes.shikimori.data.repository.rates.PinnedRateRepository
import com.gnoemes.shikimori.data.repository.rates.PinnedRateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module(includes = [AnimeProgressModule::class])
interface SyncModule {

    @Binds
    @Reusable
    fun bindRateResponseConverter(responseConverter: RateResponseConverterImpl): RateResponseConverter

    @Binds
    @Reusable
    fun bindAnimeRateSyncDbSource(source: AnimeRateSyncDbSourceImpl): AnimeRateSyncDbSource

    @Binds
    @Reusable
    fun bindMangaRateSyncDbSource(source : MangaRateSyncDbSourceImpl) : MangaRateSyncDbSource

    @Binds
    @Reusable
    fun bindEpisodeDbSource(source: EpisodeDbSourceImpl): EpisodeDbSource

    @Binds
    @Reusable
    fun bindChapterDbSource(source: ChapterDbSourceImpl) : ChapterDbSource

    @Binds
    @Reusable
    fun bindPinnedSource(source : PinnedRateDbSourceImpl) : PinnedRateDbSource

    @Binds
    @Reusable
    fun bindPinnedRepo(repo : PinnedRateRepositoryImpl) : PinnedRateRepository

}
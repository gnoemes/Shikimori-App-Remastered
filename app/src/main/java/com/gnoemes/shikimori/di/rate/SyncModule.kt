package com.gnoemes.shikimori.di.rate

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.ChapterDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.local.db.MangaRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.impl.AnimeRateSyncDbSourceImpl
import com.gnoemes.shikimori.data.local.db.impl.ChapterDbSourceImpl
import com.gnoemes.shikimori.data.local.db.impl.EpisodeDbSourceImpl
import com.gnoemes.shikimori.data.local.db.impl.MangaRateSyncDbSourceImpl
import com.gnoemes.shikimori.data.repository.common.RateResponseConverter
import com.gnoemes.shikimori.data.repository.common.impl.RateResponseConverterImpl
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

}
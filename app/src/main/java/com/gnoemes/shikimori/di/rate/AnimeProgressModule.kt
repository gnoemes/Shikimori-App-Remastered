package com.gnoemes.shikimori.di.rate

import com.gnoemes.shikimori.data.local.db.TranslationSettingDbSource
import com.gnoemes.shikimori.data.local.db.impl.TranslationSettingDbSourceImpl
import com.gnoemes.shikimori.data.repository.progress.AnimeProgressRepository
import com.gnoemes.shikimori.data.repository.progress.AnimeProgressRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface AnimeProgressModule {

    @Binds
    @Reusable
    fun bindAnimeProgressRepository(repository: AnimeProgressRepositoryImpl): AnimeProgressRepository

    @Binds
    @Reusable
    fun bindTranslationSettingSoure(source : TranslationSettingDbSourceImpl) : TranslationSettingDbSource
}
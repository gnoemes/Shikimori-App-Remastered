package com.gnoemes.shikimori.di.manga

import com.gnoemes.shikimori.data.repository.manga.MangaRepository
import com.gnoemes.shikimori.data.repository.manga.MangaRepositoryImpl
import com.gnoemes.shikimori.data.repository.ranobe.RanobeRepository
import com.gnoemes.shikimori.data.repository.ranobe.RanobeRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface MangaRepositoryModule {

    @Binds
    fun bindMangaRepository(repository : MangaRepositoryImpl) : MangaRepository

    @Binds
    fun bindRanobeRepository(repository : RanobeRepositoryImpl) : RanobeRepository

}
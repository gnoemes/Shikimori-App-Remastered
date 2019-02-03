package com.gnoemes.shikimori.di.manga

import com.gnoemes.shikimori.domain.manga.MangaInteractor
import com.gnoemes.shikimori.domain.manga.MangaInteractorImpl
import com.gnoemes.shikimori.domain.ranobe.RanobeInteractor
import com.gnoemes.shikimori.domain.ranobe.RanobeInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface MangaInteractorModule {

    @Binds
    fun bindMangaInteractor(interactor : MangaInteractorImpl) : MangaInteractor

    @Binds
    fun bindRanobeInteractor(interactor : RanobeInteractorImpl) : RanobeInteractor
}
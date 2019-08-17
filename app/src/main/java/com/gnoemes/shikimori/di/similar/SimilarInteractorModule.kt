package com.gnoemes.shikimori.di.similar

import com.gnoemes.shikimori.domain.similar.SimilarInteractor
import com.gnoemes.shikimori.domain.similar.SimilarInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface SimilarInteractorModule {
    @Binds
    fun bindInteractor(interactor: SimilarInteractorImpl): SimilarInteractor
}
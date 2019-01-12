package com.gnoemes.shikimori.di.topic

import com.gnoemes.shikimori.domain.topic.TopicInteractor
import com.gnoemes.shikimori.domain.topic.TopicInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface TopicInteractorModule {

    @Binds
    fun bindInteractor(interactor : TopicInteractorImpl) : TopicInteractor
}
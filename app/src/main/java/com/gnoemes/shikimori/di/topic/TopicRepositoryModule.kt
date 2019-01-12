package com.gnoemes.shikimori.di.topic

import com.gnoemes.shikimori.data.repository.topic.TopicRepository
import com.gnoemes.shikimori.data.repository.topic.TopicRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface TopicRepositoryModule {

    @Binds
    fun bindRepository(rep: TopicRepositoryImpl): TopicRepository
}
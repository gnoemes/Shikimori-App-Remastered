package com.gnoemes.shikimori.di.topic

import com.gnoemes.shikimori.data.repository.topic.converter.ForumResponseConverter
import com.gnoemes.shikimori.data.repository.topic.converter.ForumResponseConverterImpl
import com.gnoemes.shikimori.data.repository.topic.converter.TopicResponseConverter
import com.gnoemes.shikimori.data.repository.topic.converter.TopicResponseConverterImpl
import com.gnoemes.shikimori.presentation.presenter.topic.converter.TopicViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.topic.converter.TopicViewModelConverterImpl
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProvider
import com.gnoemes.shikimori.presentation.presenter.topic.provider.TopicResourceProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface TopicUtilModule {

    @Binds
    fun bindTopicViewModelConverter(converter: TopicViewModelConverterImpl): TopicViewModelConverter

    @Binds
    fun bindTopicResourceProvider(provider: TopicResourceProviderImpl): TopicResourceProvider

    @Binds
    fun bindForumResponseConverter(converter: ForumResponseConverterImpl): ForumResponseConverter

    @Binds
    fun bindTopicResponseConverter(converter: TopicResponseConverterImpl): TopicResponseConverter
}
package com.gnoemes.shikimori.di.comment

import com.gnoemes.shikimori.data.repository.comments.CommentRepository
import com.gnoemes.shikimori.data.repository.comments.CommentRepositoryImpl
import com.gnoemes.shikimori.data.repository.comments.converter.CommentResponseConverter
import com.gnoemes.shikimori.data.repository.comments.converter.CommentResponseConverterImpl
import com.gnoemes.shikimori.domain.comment.CommentInteractor
import com.gnoemes.shikimori.domain.comment.CommentInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface CommentModule {

    @Binds
    fun bindInteractor(interactor: CommentInteractorImpl): CommentInteractor

    @Binds
    fun bindResponseConverter(converter: CommentResponseConverterImpl): CommentResponseConverter

    @Binds
    fun bindRepository(repository: CommentRepositoryImpl) : CommentRepository

}
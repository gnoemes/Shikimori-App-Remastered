package com.gnoemes.shikimori.di.forum

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomScope
import com.gnoemes.shikimori.di.topic.TopicInteractorModule
import com.gnoemes.shikimori.di.topic.TopicRepositoryModule
import com.gnoemes.shikimori.di.topic.TopicUtilModule
import com.gnoemes.shikimori.presentation.presenter.forum.ForumPresenter
import com.gnoemes.shikimori.presentation.view.forum.ForumFragment
import com.gnoemes.shikimori.presentation.view.forum.converter.ForumConverter
import com.gnoemes.shikimori.presentation.view.forum.converter.ForumConverterImpl
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    TopicInteractorModule::class,
    TopicRepositoryModule::class,
    TopicUtilModule::class
])
interface ForumModule {

    @Binds
    fun bindConverter(converter: ForumConverterImpl): ForumConverter

    @Binds
    fun bindPresenter(presenter: ForumPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomScope
    fun bindFragment(fragment: ForumFragment): Fragment
}
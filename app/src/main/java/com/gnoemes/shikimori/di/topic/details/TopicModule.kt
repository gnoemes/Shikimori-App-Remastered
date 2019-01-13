package com.gnoemes.shikimori.di.topic.details

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomScope
import com.gnoemes.shikimori.di.comment.CommentModule
import com.gnoemes.shikimori.di.topic.TopicInteractorModule
import com.gnoemes.shikimori.di.topic.TopicRepositoryModule
import com.gnoemes.shikimori.di.topic.TopicUtilModule
import com.gnoemes.shikimori.presentation.presenter.topic.details.TopicPresenter
import com.gnoemes.shikimori.presentation.view.topic.details.TopicFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    TopicInteractorModule::class,
    TopicRepositoryModule::class,
    TopicUtilModule::class,
    CommentModule::class
])
interface TopicModule {

    @Binds
    fun bindPresenter(presenter: TopicPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomScope
    fun bindFragment(fragment: TopicFragment): Fragment


}
package com.gnoemes.shikimori.di.topic.list

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomScope
import com.gnoemes.shikimori.di.topic.TopicInteractorModule
import com.gnoemes.shikimori.di.topic.TopicRepositoryModule
import com.gnoemes.shikimori.di.topic.TopicUtilModule
import com.gnoemes.shikimori.presentation.presenter.topic.list.TopicListPresenter
import com.gnoemes.shikimori.presentation.view.topic.list.TopicListFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    TopicUtilModule::class,
    TopicRepositoryModule::class,
    TopicInteractorModule::class
])
interface TopicListModule {

    @Binds
    fun bindPresenter(presenter: TopicListPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomScope
    fun bindFragment(fragment: TopicListFragment): Fragment
}
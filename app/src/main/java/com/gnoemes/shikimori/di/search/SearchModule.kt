package com.gnoemes.shikimori.di.search

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.presentation.presenter.search.SearchPresenter
import com.gnoemes.shikimori.presentation.view.search.SearchFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    SearchRepositoryModule::class,
    SearchInteractorModule::class,
    SearchUtilModule::class
])
interface SearchModule {

    @Binds
    fun bindMvpPresenter(presenter: SearchPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: SearchFragment): Fragment

}
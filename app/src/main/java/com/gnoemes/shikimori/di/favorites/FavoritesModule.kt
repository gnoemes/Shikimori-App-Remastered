package com.gnoemes.shikimori.di.favorites

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.favorites.FavoritesPresenter
import com.gnoemes.shikimori.presentation.presenter.favorites.converter.FavoriteViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.favorites.converter.FavoriteViewModelConverterImpl
import com.gnoemes.shikimori.presentation.view.favorites.FavoritesFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    UserInteractorModule::class,
    UserUtilModule::class
])
interface FavoritesModule {

    @Binds
    fun bindConverter(converter: FavoriteViewModelConverterImpl): FavoriteViewModelConverter

    @Binds
    fun bindPresenter(presenter: FavoritesPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: FavoritesFragment): Fragment

}
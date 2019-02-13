package com.gnoemes.shikimori.di.friends

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.friends.FriendsPresenter
import com.gnoemes.shikimori.presentation.presenter.friends.converter.FriendsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.friends.converter.FriendsViewModelConverterImpl
import com.gnoemes.shikimori.presentation.view.friends.FriendsFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    UserInteractorModule::class,
    UserUtilModule::class
])
interface FriendsModule {

    @Binds
    fun provideConverter(converter: FriendsViewModelConverterImpl): FriendsViewModelConverter

    @Binds
    fun bindPresenter(presenter: FriendsPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: FriendsFragment): Fragment
}
package com.gnoemes.shikimori.di.club

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.clubs.UserClubsPresenter
import com.gnoemes.shikimori.presentation.presenter.clubs.converter.UserClubViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.clubs.converter.UserClubViewModelConverterImpl
import com.gnoemes.shikimori.presentation.view.clubs.UserClubsFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    UserInteractorModule::class,
    UserUtilModule::class
])
interface UserClubsModule {

    @Binds
    fun provideConverter(converter: UserClubViewModelConverterImpl): UserClubViewModelConverter

    @Binds
    fun bindPresenter(presenter: UserClubsPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: UserClubsFragment): Fragment
}
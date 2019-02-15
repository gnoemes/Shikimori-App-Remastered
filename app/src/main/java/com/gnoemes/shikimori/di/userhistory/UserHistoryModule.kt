package com.gnoemes.shikimori.di.userhistory

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.userhistory.UserHistoryPresenter
import com.gnoemes.shikimori.presentation.presenter.userhistory.conveter.UserHistoryViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.userhistory.conveter.UserHistoryViewModelConverterImpl
import com.gnoemes.shikimori.presentation.view.userhistory.UserHistoryFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    UserInteractorModule::class,
    UserUtilModule::class
])
interface UserHistoryModule {

    @Binds
    fun bindConverter(converter: UserHistoryViewModelConverterImpl): UserHistoryViewModelConverter

    @Binds
    fun bindPresenter(presenter: UserHistoryPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: UserHistoryFragment): Fragment
}
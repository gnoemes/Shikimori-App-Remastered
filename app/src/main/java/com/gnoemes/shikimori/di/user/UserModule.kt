package com.gnoemes.shikimori.di.user

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope
import com.gnoemes.shikimori.presentation.presenter.user.UserPresenter
import com.gnoemes.shikimori.presentation.view.user.UserFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    UserInteractorModule::class,
    UserUtilModule::class
])
interface UserModule {

    @Binds
    fun bindPresenter(presenter: UserPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    fun bindFragment(fragment: UserFragment): Fragment
}
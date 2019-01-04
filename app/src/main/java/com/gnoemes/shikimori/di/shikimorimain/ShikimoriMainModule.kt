package com.gnoemes.shikimori.di.shikimorimain

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomScope
import com.gnoemes.shikimori.presentation.presenter.shikimorimain.ShikimoriMainPresenter
import com.gnoemes.shikimori.presentation.view.shikimorimain.ShikimoriMainFragment
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
interface ShikimoriMainModule {

    @Binds
    fun bindPresenter(presenter: ShikimoriMainPresenter): MvpPresenter<*>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomScope
    fun bindFragment(mainFragment: ShikimoriMainFragment): Fragment
}
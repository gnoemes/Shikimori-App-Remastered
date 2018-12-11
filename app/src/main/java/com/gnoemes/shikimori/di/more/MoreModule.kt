package com.gnoemes.shikimori.di.more

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpPresenter
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule
import com.gnoemes.shikimori.di.base.scopes.BottomScope
import com.gnoemes.shikimori.di.user.UserInteractorModule
import com.gnoemes.shikimori.di.user.UserUtilModule
import com.gnoemes.shikimori.presentation.presenter.more.MorePresenter
import com.gnoemes.shikimori.presentation.presenter.more.provider.MoreResourceProvider
import com.gnoemes.shikimori.presentation.presenter.more.provider.MoreResourceProviderImpl
import com.gnoemes.shikimori.presentation.view.more.MoreFragment
import com.gnoemes.shikimori.presentation.view.more.MoreView
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module(includes = [
    BaseChildFragmentModule::class,
    UserUtilModule::class,
    UserInteractorModule::class
])
interface MoreModule {

    @Binds
    fun bindResourecProvider(resourceProvider: MoreResourceProviderImpl) : MoreResourceProvider

    @Binds
    fun bindPresenter(morePresenter : MorePresenter) : MvpPresenter<MoreView>

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomScope
    fun bindFragment(moreFragment: MoreFragment): Fragment
}
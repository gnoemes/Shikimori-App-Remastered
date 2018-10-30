package com.gnoemes.shikimori.di.rate;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.presentation.presenter.rates.RatePresenter;
import com.gnoemes.shikimori.presentation.view.rates.RateFragment;

import javax.inject.Named;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;

@Module(includes = {

})
public interface RateModule {


    @Binds
    MvpPresenter bindMvpPresenter(RatePresenter presenter);

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    Fragment bindFragment(RateFragment fragment);
}

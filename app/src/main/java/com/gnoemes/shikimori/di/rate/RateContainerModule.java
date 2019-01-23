package com.gnoemes.shikimori.di.rate;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.di.series.SeriesInteractorModule;
import com.gnoemes.shikimori.di.series.SeriesRepositoryModule;
import com.gnoemes.shikimori.di.series.SeriesUtilModule;
import com.gnoemes.shikimori.di.user.UserInteractorModule;
import com.gnoemes.shikimori.di.user.UserUtilModule;
import com.gnoemes.shikimori.presentation.presenter.rates.RatesContainerPresenter;
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverter;
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverterImpl;
import com.gnoemes.shikimori.presentation.view.rates.RateFragment;
import com.gnoemes.shikimori.presentation.view.rates.RatesContainerFragment;

import javax.inject.Named;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.Reusable;
import dagger.android.ContributesAndroidInjector;

@Module(includes = {
        BaseChildFragmentModule.class,
        SyncModule.class,
        SeriesUtilModule.class,
        SeriesRepositoryModule.class,
        SeriesInteractorModule.class,
        UserInteractorModule.class,
        UserUtilModule.class,
        RateInteractorModule.class,
        RateUtilModule.class
})
public interface RateContainerModule {

    @Binds
    @Reusable
    RateCountConverter bindRateCountConverter(RateCountConverterImpl converter);

    @Binds
    MvpPresenter bindMvpPresenter(RatesContainerPresenter presenter);

    @ContributesAndroidInjector(modules = RateModule.class)
    RateFragment rateFragmentInjector();

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    Fragment bindFragment(RatesContainerFragment fragment);
}
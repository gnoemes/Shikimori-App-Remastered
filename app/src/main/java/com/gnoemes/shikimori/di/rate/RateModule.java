package com.gnoemes.shikimori.di.rate;

import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.data.local.preference.RateSortSource;
import com.gnoemes.shikimori.data.local.preference.impl.RateSortSourceImpl;
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.di.series.SeriesInteractorModule;
import com.gnoemes.shikimori.di.series.SeriesRepositoryModule;
import com.gnoemes.shikimori.di.series.SeriesUtilModule;
import com.gnoemes.shikimori.di.user.UserInteractorModule;
import com.gnoemes.shikimori.di.user.UserUtilModule;
import com.gnoemes.shikimori.presentation.presenter.rates.RatePresenter;
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverter;
import com.gnoemes.shikimori.presentation.presenter.rates.converter.RateCountConverterImpl;
import com.gnoemes.shikimori.presentation.view.rates.RateFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

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
public interface RateModule {

    @Binds
    @Reusable
    RateCountConverter bindRateCountConverter(RateCountConverterImpl converter);

    @Binds
    @Reusable
    RateSortSource bindRateSortSource(RateSortSourceImpl source);

    @Binds
    MvpPresenter bindMvpPresenter(RatePresenter presenter);

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    Fragment bindFragment(RateFragment fragment);
}
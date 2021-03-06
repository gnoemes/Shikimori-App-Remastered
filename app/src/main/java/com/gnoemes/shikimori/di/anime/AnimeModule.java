package com.gnoemes.shikimori.di.anime;

import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpPresenter;
import com.gnoemes.shikimori.di.base.modules.BaseChildFragmentModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.di.rate.RateInteractorModule;
import com.gnoemes.shikimori.di.rate.RateModule;
import com.gnoemes.shikimori.di.rate.SyncModule;
import com.gnoemes.shikimori.di.related.RelatedModule;
import com.gnoemes.shikimori.di.series.SeriesInteractorModule;
import com.gnoemes.shikimori.di.series.SeriesRepositoryModule;
import com.gnoemes.shikimori.di.series.SeriesUtilModule;
import com.gnoemes.shikimori.di.studio.StudioUtilModule;
import com.gnoemes.shikimori.di.user.UserInteractorModule;
import com.gnoemes.shikimori.di.user.UserUtilModule;
import com.gnoemes.shikimori.presentation.presenter.anime.AnimePresenter;
import com.gnoemes.shikimori.presentation.view.anime.AnimeFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
        BaseChildFragmentModule.class,
        AnimeUtilModule.class,
        AnimeRepositoryModule.class,
        AnimeInteractorModule.class,
        RateModule.class,
        UserUtilModule.class,
        UserInteractorModule.class,
        StudioUtilModule.class,
        RelatedModule.class,
        RateInteractorModule.class,
        SyncModule.class,
        SeriesUtilModule.class,
        SeriesRepositoryModule.class,
        SeriesInteractorModule.class
})
public interface AnimeModule {

    @Binds
    MvpPresenter bindPresenter(AnimePresenter presenter);

    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @BottomChildScope
    Fragment bindFragment(AnimeFragment fragment);
}

package com.gnoemes.shikimori.di;

import com.gnoemes.shikimori.di.series.SeriesSyncServiceModule;
import com.gnoemes.shikimori.domain.series.SeriesSyncService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ServiceInjectionModule {

    @ContributesAndroidInjector(modules = SeriesSyncServiceModule.class)
    SeriesSyncService seriesSyncService();

}

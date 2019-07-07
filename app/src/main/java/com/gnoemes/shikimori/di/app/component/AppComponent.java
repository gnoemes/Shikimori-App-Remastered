package com.gnoemes.shikimori.di.app.component;

import com.gnoemes.shikimori.App;
import com.gnoemes.shikimori.di.ActivityInjectionModule;
import com.gnoemes.shikimori.di.ServiceInjectionModule;
import com.gnoemes.shikimori.di.app.module.AppModule;
import com.gnoemes.shikimori.di.app.module.ConverterModule;
import com.gnoemes.shikimori.di.app.module.InteractorModule;
import com.gnoemes.shikimori.di.app.module.NavigationModule;
import com.gnoemes.shikimori.di.app.module.RepositoryModule;
import com.gnoemes.shikimori.di.app.module.UtilModule;
import com.gnoemes.shikimori.di.app.module.local.DbModule;
import com.gnoemes.shikimori.di.app.module.local.HistoryModule;
import com.gnoemes.shikimori.di.app.module.local.SettingsModule;
import com.gnoemes.shikimori.di.app.module.network.ApiModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class, ServiceInjectionModule.class,
        ActivityInjectionModule.class, NavigationModule.class,
        UtilModule.class, DbModule.class, SettingsModule.class,
        HistoryModule.class, RepositoryModule.class, InteractorModule.class,
        ConverterModule.class})
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {
    }
}

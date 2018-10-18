package com.gnoemes.shikimori.di;

import com.gnoemes.shikimori.di.auth.AuthModule;
import com.gnoemes.shikimori.di.base.scopes.ActivityScope;
import com.gnoemes.shikimori.di.main.module.MainModule;
import com.gnoemes.shikimori.presentation.view.auth.AuthActivity;
import com.gnoemes.shikimori.presentation.view.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ActivityInjectionModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainModule.class)
    MainActivity mainActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = AuthModule.class)
    AuthActivity authActivityInjector();
}

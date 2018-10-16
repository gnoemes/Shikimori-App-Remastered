package com.gnoemes.shikimori.di;

import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.di.calendar.CalendarModule;
import com.gnoemes.shikimori.presentation.view.calendar.CalendarFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface FragmentInjectionModule {

    @BottomChildScope
    @ContributesAndroidInjector(modules = CalendarModule.class)
    CalendarFragment calendarFragmentInjector();
}

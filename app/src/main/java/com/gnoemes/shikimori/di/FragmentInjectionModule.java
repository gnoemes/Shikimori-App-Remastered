package com.gnoemes.shikimori.di;

import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.di.calendar.CalendarModule;
import com.gnoemes.shikimori.di.character.CharacterModule;
import com.gnoemes.shikimori.di.person.PersonModule;
import com.gnoemes.shikimori.di.rate.RateContainerModule;
import com.gnoemes.shikimori.presentation.view.calendar.CalendarFragment;
import com.gnoemes.shikimori.presentation.view.character.CharacterFragment;
import com.gnoemes.shikimori.presentation.view.person.PersonFragment;
import com.gnoemes.shikimori.presentation.view.rates.RatesContainerFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface FragmentInjectionModule {

    @BottomChildScope
    @ContributesAndroidInjector(modules = CalendarModule.class)
    CalendarFragment calendarFragmentInjector();

    @BottomChildScope
    @ContributesAndroidInjector(modules = CharacterModule.class)
    CharacterFragment characterFragmentInjector();

    @BottomChildScope
    @ContributesAndroidInjector(modules = PersonModule.class)
    PersonFragment personFragmentInjector();

    @BottomChildScope
    @ContributesAndroidInjector(modules = RateContainerModule.class)
    RatesContainerFragment ratesContainerFragment();
}

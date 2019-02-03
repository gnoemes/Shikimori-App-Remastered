package com.gnoemes.shikimori.di;

import com.gnoemes.shikimori.di.anime.AnimeModule;
import com.gnoemes.shikimori.di.base.scopes.BottomChildScope;
import com.gnoemes.shikimori.di.calendar.CalendarModule;
import com.gnoemes.shikimori.di.character.CharacterModule;
import com.gnoemes.shikimori.di.manga.MangaModule;
import com.gnoemes.shikimori.di.more.MoreModule;
import com.gnoemes.shikimori.di.person.PersonModule;
import com.gnoemes.shikimori.di.rate.RateContainerModule;
import com.gnoemes.shikimori.di.search.SearchModule;
import com.gnoemes.shikimori.di.series.EpisodeModule;
import com.gnoemes.shikimori.di.series.TranslationModule;
import com.gnoemes.shikimori.di.shikimorimain.ShikimoriMainModule;
import com.gnoemes.shikimori.di.topic.details.TopicModule;
import com.gnoemes.shikimori.di.topic.list.TopicListModule;
import com.gnoemes.shikimori.presentation.view.anime.AnimeFragment;
import com.gnoemes.shikimori.presentation.view.calendar.CalendarFragment;
import com.gnoemes.shikimori.presentation.view.character.CharacterFragment;
import com.gnoemes.shikimori.presentation.view.manga.MangaFragment;
import com.gnoemes.shikimori.presentation.view.more.MoreFragment;
import com.gnoemes.shikimori.presentation.view.person.PersonFragment;
import com.gnoemes.shikimori.presentation.view.rates.RatesContainerFragment;
import com.gnoemes.shikimori.presentation.view.search.SearchFragment;
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesFragment;
import com.gnoemes.shikimori.presentation.view.series.translations.TranslationsFragment;
import com.gnoemes.shikimori.presentation.view.shikimorimain.ShikimoriMainFragment;
import com.gnoemes.shikimori.presentation.view.topic.details.TopicFragment;
import com.gnoemes.shikimori.presentation.view.topic.list.TopicListFragment;

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
    @ContributesAndroidInjector(modules = {AnimeModule.class})
    AnimeFragment animeFragmentInjector();

    @BottomChildScope
    @ContributesAndroidInjector(modules = RateContainerModule.class)
    RatesContainerFragment ratesContainerFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = SearchModule.class)
    SearchFragment searchFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = MoreModule.class)
    MoreFragment moreFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = ShikimoriMainModule.class)
    ShikimoriMainFragment mainFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = TopicListModule.class)
    TopicListFragment topicListFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = TopicModule.class)
    TopicFragment topicFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = EpisodeModule.class)
    EpisodesFragment episodeFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = TranslationModule.class)
    TranslationsFragment translationsFragment();

    @BottomChildScope
    @ContributesAndroidInjector(modules = MangaModule.class)
    MangaFragment mangaFragment();
}

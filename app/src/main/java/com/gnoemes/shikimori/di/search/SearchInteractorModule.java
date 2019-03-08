package com.gnoemes.shikimori.di.search;

import com.gnoemes.shikimori.domain.search.SearchInteractor;
import com.gnoemes.shikimori.domain.search.SearchInteractorImpl;
import com.gnoemes.shikimori.domain.search.filter.FilterInteractor;
import com.gnoemes.shikimori.domain.search.filter.FilterInteractorImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface SearchInteractorModule {

    @Binds
    SearchInteractor bindSearchInteractor(SearchInteractorImpl interactor);

    @Binds
    FilterInteractor bindFilterInteractor(FilterInteractorImpl interactor);
}

package com.gnoemes.shikimori.di.search;

import com.gnoemes.shikimori.data.local.services.FilterSource;
import com.gnoemes.shikimori.data.local.services.impl.FilterSourceImpl;
import com.gnoemes.shikimori.data.repository.search.FilterRepository;
import com.gnoemes.shikimori.data.repository.search.FilterRepositoryImpl;
import com.gnoemes.shikimori.data.repository.search.SearchRepository;
import com.gnoemes.shikimori.data.repository.search.SearchRepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface SearchRepositoryModule {
    @Binds
    SearchRepository bindSearchRepository(SearchRepositoryImpl searchRepository);

    @Binds
    FilterRepository bindFilterRepository(FilterRepositoryImpl repository);

    @Binds
    FilterSource bindFilterSource(FilterSourceImpl source);
}

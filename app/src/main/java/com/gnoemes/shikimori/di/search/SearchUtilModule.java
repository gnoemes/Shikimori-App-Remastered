package com.gnoemes.shikimori.di.search;

import com.gnoemes.shikimori.domain.search.SearchQueryBuilder;
import com.gnoemes.shikimori.domain.search.SearchQueryBuilderImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface SearchUtilModule {

    @Binds
    SearchQueryBuilder bbindSearchQueryBuilder(SearchQueryBuilderImpl builder);
}

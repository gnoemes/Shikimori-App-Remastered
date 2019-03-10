package com.gnoemes.shikimori.di.search;

import com.gnoemes.shikimori.domain.search.SearchQueryBuilder;
import com.gnoemes.shikimori.domain.search.SearchQueryBuilderImpl;
import com.gnoemes.shikimori.presentation.presenter.search.converter.SearchViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.search.converter.SearchViewModelConverterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface SearchUtilModule {

    @Binds
    SearchQueryBuilder bbindSearchQueryBuilder(SearchQueryBuilderImpl builder);

    @Binds
    SearchViewModelConverter bindSearchViewModelConverter(SearchViewModelConverterImpl converter);

}

package com.gnoemes.shikimori.di.anime;


import com.gnoemes.shikimori.data.repository.anime.converter.AnimeDetailsResponseConverter;
import com.gnoemes.shikimori.data.repository.anime.converter.AnimeDetailsResponseConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.common.converter.DetailsContentViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.common.converter.FranchiseNodeViewModelConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.common.converter.LinkViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.common.converter.LinkViewModelConverterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface AnimeUtilModule {

    @Binds
    @Reusable
    AnimeDetailsViewModelConverter bindAnimeDetailsViewModelConverter(AnimeDetailsViewModelConverterImpl converter);

    @Binds
    @Reusable
    AnimeDetailsResponseConverter bindAnimeDetailsResponseConverter(AnimeDetailsResponseConverterImpl responseConverter);

    @Binds
    @Reusable
    LinkViewModelConverter bindLinkViewModelConverter(LinkViewModelConverterImpl converter);

    @Binds
    @Reusable
    FranchiseNodeViewModelConverter bindFranchiseNodeViewModelConverter(FranchiseNodeViewModelConverterImpl converter);

    @Binds
    @Reusable
    DetailsContentViewModelConverter bindDetailsContentViewModelConverter(DetailsContentViewModelConverterImpl converter);
}

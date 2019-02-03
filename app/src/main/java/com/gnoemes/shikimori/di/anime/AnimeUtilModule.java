package com.gnoemes.shikimori.di.anime;


import com.gnoemes.shikimori.data.repository.anime.converter.AnimeDetailsResponseConverter;
import com.gnoemes.shikimori.data.repository.anime.converter.AnimeDetailsResponseConverterImpl;
import com.gnoemes.shikimori.di.details.DetailsUtilModule;
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.anime.converter.AnimeDetailsViewModelConverterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module(includes = DetailsUtilModule.class)
public interface AnimeUtilModule {

    @Binds
    @Reusable
    AnimeDetailsViewModelConverter bindAnimeDetailsViewModelConverter(AnimeDetailsViewModelConverterImpl converter);

    @Binds
    @Reusable
    AnimeDetailsResponseConverter bindAnimeDetailsResponseConverter(AnimeDetailsResponseConverterImpl responseConverter);

}

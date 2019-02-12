package com.gnoemes.shikimori.di.user;

import com.gnoemes.shikimori.data.repository.user.converter.FavoriteListResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.FavoriteListResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.MessageResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.MessageResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.UserDetailsResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.UserDetailsResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.UserHistoryConverter;
import com.gnoemes.shikimori.data.repository.user.converter.UserHistoryConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.user.converter.UserDetailsViewModelConverter;
import com.gnoemes.shikimori.presentation.presenter.user.converter.UserDetailsViewModelConverterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface UserUtilModule {

    @Binds
    @Reusable
    UserDetailsResponseConverter bindUserDetailsResponseConverter(UserDetailsResponseConverterImpl converter);

    @Binds
    @Reusable
    UserHistoryConverter bindUserHistoryConverter(UserHistoryConverterImpl converter);

    @Binds
    @Reusable
    FavoriteListResponseConverter bindFavoriteListResponseConverter(FavoriteListResponseConverterImpl converter);

    @Binds
    @Reusable
    MessageResponseConverter bindMessageResponseConverter(MessageResponseConverterImpl converter);

    @Binds
    @Reusable
    UserDetailsViewModelConverter bindUserDetailsViewModelConverter(UserDetailsViewModelConverterImpl converter);
}

package com.gnoemes.shikimori.di.user;

import com.gnoemes.shikimori.data.repository.user.converter.FavoriteListResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.FavoriteListResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.MessageResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.MessageResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.UserDetailsResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.UserDetailsResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.UserHistoryConverter;
import com.gnoemes.shikimori.data.repository.user.converter.UserHistoryConverterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface UserUtilModule {

    @Binds
    UserDetailsResponseConverter bindUserDetailsResponseConverter(UserDetailsResponseConverterImpl converter);

    @Binds
    UserHistoryConverter bindUserHistoryConverter(UserHistoryConverterImpl converter);

    @Binds
    FavoriteListResponseConverter bindFavoriteListResponseConverter(FavoriteListResponseConverterImpl converter);

    @Binds
    MessageResponseConverter bindMessageResponseConverter(MessageResponseConverterImpl converter);
}

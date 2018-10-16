package com.gnoemes.shikimori.di.app.module;

import com.gnoemes.shikimori.data.repository.app.AuthorizationRepository;
import com.gnoemes.shikimori.data.repository.app.TokenRepository;
import com.gnoemes.shikimori.data.repository.app.TokenSource;
import com.gnoemes.shikimori.data.repository.app.impl.AuthorizationRepositoryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.TokenRepositoryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.TokenSourceImpl;
import com.gnoemes.shikimori.data.repository.rates.RatesRepository;
import com.gnoemes.shikimori.data.repository.rates.RatesRepositoryImpl;
import com.gnoemes.shikimori.data.repository.user.UserRepository;
import com.gnoemes.shikimori.data.repository.user.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface RepositoryModule {

    @Binds
    @Singleton
    TokenRepository bindTokenRepository(TokenRepositoryImpl repository);

    @Binds
    @Singleton
    TokenSource bindTokenSource(TokenSourceImpl source);

    @Binds
    @Reusable
    UserRepository bindUserRepository(UserRepositoryImpl repository);

    @Binds
    @Reusable
    RatesRepository bindRatesRepository(RatesRepositoryImpl repository);

    @Binds
    @Reusable
    AuthorizationRepository bindAuthorizationRepository(AuthorizationRepositoryImpl repository);

}

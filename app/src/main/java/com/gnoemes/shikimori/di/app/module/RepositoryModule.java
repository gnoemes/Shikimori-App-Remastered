package com.gnoemes.shikimori.di.app.module;

import com.gnoemes.shikimori.data.repository.app.AuthorizationRepository;
import com.gnoemes.shikimori.data.repository.app.TokenRepository;
import com.gnoemes.shikimori.data.repository.app.TokenSource;
import com.gnoemes.shikimori.data.repository.app.impl.AuthorizationRepositoryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.TokenRepositoryImpl;
import com.gnoemes.shikimori.data.repository.app.impl.TokenSourceImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface RepositoryModule {

    @Binds
    TokenRepository bindTokenRepository(TokenRepositoryImpl repository);

    @Binds
    TokenSource bindTokenSource(TokenSourceImpl source);

    @Binds
    AuthorizationRepository bindAuthorizationRepository(AuthorizationRepositoryImpl repository);
}

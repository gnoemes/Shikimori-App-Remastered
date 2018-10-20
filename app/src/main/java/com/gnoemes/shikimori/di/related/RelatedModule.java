package com.gnoemes.shikimori.di.related;

import com.gnoemes.shikimori.data.repository.common.RelatedResponseConverter;
import com.gnoemes.shikimori.data.repository.common.impl.RelatedResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.related.RelatedRepository;
import com.gnoemes.shikimori.data.repository.related.RelatedRepositoryImpl;
import com.gnoemes.shikimori.domain.related.RelatedInteractor;
import com.gnoemes.shikimori.domain.related.RelatedInteractorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface RelatedModule {

    @Binds
    @Reusable
    RelatedInteractor bindRelatedInteractor(RelatedInteractorImpl interactor);

    @Binds
    @Reusable
    RelatedRepository bindRelatedRepository(RelatedRepositoryImpl repository);

    @Binds
    @Reusable
    RelatedResponseConverter bindRelatedResponseConverter(RelatedResponseConverterImpl converter);

}

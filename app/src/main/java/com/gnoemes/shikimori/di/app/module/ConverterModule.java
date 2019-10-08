package com.gnoemes.shikimori.di.app.module;

import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter;
import com.gnoemes.shikimori.data.repository.common.CharacterResponseConverter;
import com.gnoemes.shikimori.data.repository.common.FranchiseResponseConverter;
import com.gnoemes.shikimori.data.repository.common.GenreResponseConverter;
import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter;
import com.gnoemes.shikimori.data.repository.common.LinkResponseConverter;
import com.gnoemes.shikimori.data.repository.common.LinkedContentResponseConverter;
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter;
import com.gnoemes.shikimori.data.repository.common.PersonResponseConverter;
import com.gnoemes.shikimori.data.repository.common.RolesResponseConverter;
import com.gnoemes.shikimori.data.repository.common.impl.AnimeResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.CharacterResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.FranchiseResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.GenreResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.ImageResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.LinkResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.LinkedContentResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.MangaResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.PersonResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.common.impl.RolesResponseConverterImpl;
import com.gnoemes.shikimori.data.repository.user.converter.UserBriefResponseConverter;
import com.gnoemes.shikimori.data.repository.user.converter.UserBriefResponseConverterImpl;
import com.gnoemes.shikimori.presentation.presenter.common.converter.BBCodesTextProcessor;
import com.gnoemes.shikimori.presentation.presenter.common.converter.KefirBBCodesTextProcessorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface ConverterModule {

    @Binds
    @Reusable
    LinkResponseConverter bindLinkResponseConverter(LinkResponseConverterImpl converter);

    @Binds
    @Reusable
    AnimeResponseConverter bindAnimeResponseConverter(AnimeResponseConverterImpl converter);

    @Binds
    @Reusable
    CharacterResponseConverter bindCharacterResponseConverter(CharacterResponseConverterImpl converter);

    @Binds
    @Reusable
    RolesResponseConverter bindRolesResponseConverter(RolesResponseConverterImpl converter);

    @Binds
    @Reusable
    FranchiseResponseConverter bindFranchiseResponseConverter(FranchiseResponseConverterImpl converter);

    @Binds
    @Reusable
    GenreResponseConverter bindGenreResponseConverter(GenreResponseConverterImpl converter);

    @Binds
    @Reusable
    ImageResponseConverter bindImageResponseConverter(ImageResponseConverterImpl converter);

    @Binds
    @Reusable
    MangaResponseConverter bindMangaResponseConverter(MangaResponseConverterImpl converter);

    @Binds
    @Reusable
    UserBriefResponseConverter bindUserBriefResponseConverter(UserBriefResponseConverterImpl converter);

    @Binds
    @Reusable
    LinkedContentResponseConverter bindLinkedContentResponseConverter(LinkedContentResponseConverterImpl converter);

    @Binds
    @Reusable
    PersonResponseConverter bindPersonResponseConverter(PersonResponseConverterImpl converter);

    @Binds
    @Reusable
    BBCodesTextProcessor bindBbCodesTextProcessor(KefirBBCodesTextProcessorImpl processor);

}

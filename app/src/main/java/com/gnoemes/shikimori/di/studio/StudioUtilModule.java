package com.gnoemes.shikimori.di.studio;

import com.gnoemes.shikimori.data.repository.studio.StudioResponseConverter;
import com.gnoemes.shikimori.data.repository.studio.StudioResponseConverterImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface StudioUtilModule {

    @Binds
    @Reusable
    StudioResponseConverter bindStudioResponseConverter(StudioResponseConverterImpl converter);
}

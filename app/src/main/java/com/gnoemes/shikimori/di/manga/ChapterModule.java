package com.gnoemes.shikimori.di.manga;

import com.gnoemes.shikimori.data.local.db.ChapterDbSource;
import com.gnoemes.shikimori.data.local.db.impl.ChapterDbSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Reusable;

@Module
public interface ChapterModule {

    @Binds
    @Reusable
    ChapterDbSource bindChapterDbSource(ChapterDbSourceImpl source);


}

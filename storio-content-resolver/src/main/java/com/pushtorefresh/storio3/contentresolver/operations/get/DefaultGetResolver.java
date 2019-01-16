package com.pushtorefresh.storio3.contentresolver.operations.get;

import android.database.Cursor;

import com.pushtorefresh.storio3.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio3.contentresolver.queries.Query;

import androidx.annotation.NonNull;

/**
 * Default implementation of {@link GetResolver}, thread-safe.
 */
public abstract class DefaultGetResolver<T> extends GetResolver<T> {

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOContentResolver storIOContentResolver, @NonNull Query query) {
        return storIOContentResolver.lowLevel().query(query);
    }
}

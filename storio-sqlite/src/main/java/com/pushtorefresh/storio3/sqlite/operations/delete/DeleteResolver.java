package com.pushtorefresh.storio3.sqlite.operations.delete;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;

import androidx.annotation.NonNull;

/**
 * Defines behavior of Delete Operation.
 * <p>
 * Implementation should be thread-safe!
 */
public abstract class DeleteResolver<T> {

    /**
     * Performs delete of an object.
     *
     * @param storIOSQLite {@link StorIOSQLite} instance to perform delete on.
     * @param object       object that should be deleted.
     * @return non-null result of Delete Operation.
     */
    @NonNull
    public abstract DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull T object);
}

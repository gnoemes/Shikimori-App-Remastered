package com.pushtorefresh.storio3;

import com.pushtorefresh.storio3.internal.TypeMapping;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface for search type mapping.
 */
public interface TypeMappingFinder {
    @Nullable
    <T> TypeMapping<T> findTypeMapping(@NonNull Class<T> type);

    void directTypeMapping(@Nullable Map<Class<?>, ? extends TypeMapping<?>> directTypeMapping);

    @Nullable
    Map<Class<?>, ? extends TypeMapping<?>> directTypeMapping();
}

package com.pushtorefresh.storio3.contentresolver.impl;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import com.pushtorefresh.storio3.Interceptor;
import com.pushtorefresh.storio3.TypeMappingFinder;
import com.pushtorefresh.storio3.contentresolver.Changes;
import com.pushtorefresh.storio3.contentresolver.ContentResolverTypeMapping;
import com.pushtorefresh.storio3.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio3.contentresolver.queries.DeleteQuery;
import com.pushtorefresh.storio3.contentresolver.queries.InsertQuery;
import com.pushtorefresh.storio3.contentresolver.queries.Query;
import com.pushtorefresh.storio3.contentresolver.queries.UpdateQuery;
import com.pushtorefresh.storio3.internal.TypeMappingFinderImpl;
import com.pushtorefresh.storio3.operations.PreparedCompletableOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static com.pushtorefresh.storio3.internal.Checks.checkNotNull;
import static com.pushtorefresh.storio3.internal.Environment.RX_JAVA_2_IS_IN_THE_CLASS_PATH;
import static com.pushtorefresh.storio3.internal.Environment.throwExceptionIfRxJava2IsNotAvailable;
import static com.pushtorefresh.storio3.internal.InternalQueries.nullableArrayOfStringsFromListOfStrings;
import static com.pushtorefresh.storio3.internal.InternalQueries.nullableString;
import static java.util.Collections.unmodifiableMap;

/**
 * Default, thread-safe implementation of {@link StorIOContentResolver}.
 */
public class DefaultStorIOContentResolver extends StorIOContentResolver {

    @NonNull
    private final LowLevel lowLevel;

    @NonNull
    private final ContentResolver contentResolver;

    @NonNull
    private final Handler contentObserverHandler;

    @Nullable
    private final Scheduler defaultRxScheduler;

    @NonNull
    private final List<Interceptor> interceptors;

    protected DefaultStorIOContentResolver(@NonNull ContentResolver contentResolver,
                                           @NonNull Handler contentObserverHandler,
                                           @NonNull TypeMappingFinder typeMappingFinder,
                                           @Nullable Scheduler defaultRxScheduler,
                                           @NonNull List<Interceptor> interceptors) {
        this.contentResolver = contentResolver;
        this.contentObserverHandler = contentObserverHandler;
        this.defaultRxScheduler = defaultRxScheduler;
        this.interceptors = interceptors;
        lowLevel = new LowLevelImpl(typeMappingFinder);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Flowable<Changes> observeChangesOfUris(@NonNull final Set<Uri> uris, @NonNull BackpressureStrategy backpressureStrategy) {
        throwExceptionIfRxJava2IsNotAvailable("Observing changes in StorIOContentProvider");

        // indirect usage of RxJava
        // required to avoid problems with ClassLoader when RxJava is not in ClassPath
        return RxChangesObserver.observeChanges(contentResolver, uris, contentObserverHandler, Build.VERSION.SDK_INT, backpressureStrategy);
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public Scheduler defaultRxScheduler() {
        return defaultRxScheduler;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public LowLevel lowLevel() {
        return lowLevel;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<Interceptor> interceptors() {
        return interceptors;
    }

    /**
     * Creates new builder for {@link DefaultStorIOContentResolver}.
     *
     * @return non-null instance of {@link DefaultStorIOContentResolver.Builder}.
     */
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link DefaultStorIOContentResolver}.
     */
    public static final class Builder {

        /**
         * Please use {@link DefaultStorIOContentResolver#builder()} instead of this.
         */
        Builder() {
        }

        /**
         * Required: Specifies {@link ContentResolver} for {@link StorIOContentResolver}.
         * <p>
         * You can get in from any {@link android.content.Context}
         * instance: {@code context.getContentResolver().
         * It's safe to use {@link android.app.Activity} as {@link android.content.Context}.
         *
         * @param contentResolver non-null instance of {@link ContentResolver}.
         * @return builder.
         */
        @NonNull
        public CompleteBuilder contentResolver(@NonNull ContentResolver contentResolver) {
            checkNotNull(contentResolver, "Please specify content resolver");
            return new CompleteBuilder(contentResolver);
        }
    }

    /**
     * Compile-time safe part of builder for {@link DefaultStorIOContentResolver}.
     */
    public static final class CompleteBuilder {

        @NonNull
        private final ContentResolver contentResolver;

        @Nullable
        private Map<Class<?>, ContentResolverTypeMapping<?>> typeMapping;

        @Nullable
        private Handler contentObserverHandler;

        @Nullable
        private TypeMappingFinder typeMappingFinder;

        private Scheduler defaultRxScheduler = RX_JAVA_2_IS_IN_THE_CLASS_PATH ? Schedulers.io() : null;

        @NonNull
        private List<Interceptor> interceptors = new ArrayList<Interceptor>();

        CompleteBuilder(@NonNull ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        /**
         * Adds {@link ContentResolverTypeMapping} for some type.
         *
         * @param type        type.
         * @param typeMapping mapping for type.
         * @param <T>         type.
         * @return builder.
         */
        @NonNull
        public <T> CompleteBuilder addTypeMapping(@NonNull Class<T> type, @NonNull ContentResolverTypeMapping<T> typeMapping) {
            checkNotNull(type, "Please specify type");
            checkNotNull(typeMapping, "Please specify type mapping");

            if (this.typeMapping == null) {
                this.typeMapping = new HashMap<Class<?>, ContentResolverTypeMapping<?>>();
            }

            this.typeMapping.put(type, typeMapping);

            return this;
        }

        @NonNull
        public <T> CompleteBuilder contentObserverHandler(@NonNull Handler contentObserverHandler) {
            checkNotNull(contentObserverHandler, "contentObserverHandler should not be null");

            this.contentObserverHandler = contentObserverHandler;

            return this;
        }

        /**
         * Optional: Specifies {@link TypeMappingFinder} for low level usage.
         *
         * @param typeMappingFinder non-null custom implementation of {@link TypeMappingFinder}.
         * @return builder.
         */
        @NonNull
        public CompleteBuilder typeMappingFinder(@NonNull TypeMappingFinder typeMappingFinder) {
            checkNotNull(typeMappingFinder, "Please specify typeMappingFinder");

            this.typeMappingFinder = typeMappingFinder;

            return this;
        }

        /**
         * Provides a scheduler on which {@link io.reactivex.Flowable} / {@link io.reactivex.Single}
         * or {@link io.reactivex.Completable} will be subscribed.
         * <p/>
         * @see com.pushtorefresh.storio3.operations.PreparedOperation#asRxFlowable(BackpressureStrategy)
         * @see com.pushtorefresh.storio3.operations.PreparedOperation#asRxSingle()
         * @see PreparedCompletableOperation#asRxCompletable()
         *
         * @return the scheduler or {@code null} if it isn't needed to apply it.
         */
        @NonNull
        public CompleteBuilder defaultRxScheduler(@Nullable Scheduler defaultRxScheduler) {
            this.defaultRxScheduler = defaultRxScheduler;
            return this;
        }

        /**
         * Optional: Adds {@link Interceptor} to all database operation.
         * Multiple interceptors would be called in the order they were added.
         *
         * @param interceptor non-null custom implementation of {@link Interceptor}.
         * @return builder.
         */
        @NonNull
        public CompleteBuilder addInterceptor(@NonNull Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        /**
         * Builds new instance of {@link DefaultStorIOContentResolver}.
         *
         * @return new instance of {@link DefaultStorIOContentResolver}.
         */
        @NonNull
        public DefaultStorIOContentResolver build() {
            if (contentObserverHandler == null) {
                final HandlerThread handlerThread = new HandlerThread("StorIOContentResolverNotificationsThread");
                handlerThread.start(); // multithreading: don't block me, bro!
                contentObserverHandler = new Handler(handlerThread.getLooper());
            }

            if (typeMappingFinder == null) {
                typeMappingFinder = new TypeMappingFinderImpl();
            }
            if (typeMapping != null) {
                typeMappingFinder.directTypeMapping(unmodifiableMap(typeMapping));
            }

            return new DefaultStorIOContentResolver(contentResolver, contentObserverHandler, typeMappingFinder, defaultRxScheduler, interceptors);
        }
    }

    protected class LowLevelImpl extends LowLevel {

        @NonNull
        private final TypeMappingFinder typeMappingFinder;

        protected LowLevelImpl(@NonNull TypeMappingFinder typeMappingFinder) {
            this.typeMappingFinder = typeMappingFinder;
        }

        /**
         * Gets type mapping for required type.
         * <p>
         * This implementation can handle subclasses of types, that registered its type mapping.
         * For example: You've added type mapping for {@code User.class},
         * and you have {@code UserFromServiceA.class} which extends {@code User.class},
         * and you didn't add type mapping for {@code UserFromServiceA.class}
         * because they have same fields and you just want to have multiple classes.
         * This implementation will find type mapping of {@code User.class}
         * and use it as type mapping for {@code UserFromServiceA.class}.
         *
         * @return direct or indirect type mapping for passed type, or {@code null}.
         */
        @Nullable
        @Override
        public <T> ContentResolverTypeMapping<T> typeMapping(final @NonNull Class<T> type) {
            return (ContentResolverTypeMapping<T>) typeMappingFinder.findTypeMapping(type);
        }

        /**
         * {@inheritDoc}
         */
        @WorkerThread
        @SuppressLint("Recycle")
        @NonNull
        @Override
        public Cursor query(@NonNull Query query) {
            Cursor cursor = contentResolver.query(
                    query.uri(),
                    nullableArrayOfStringsFromListOfStrings(query.columns()),
                    nullableString(query.where()),
                    nullableArrayOfStringsFromListOfStrings(query.whereArgs()),
                    nullableString(query.sortOrder())
            );

            if (cursor == null) {
                throw new IllegalStateException("Cursor returned by content provider is null");
            }

            return cursor;
        }

        /**
         * {@inheritDoc}
         */
        @WorkerThread
        @NonNull
        @Override
        public Uri insert(@NonNull InsertQuery insertQuery, @NonNull ContentValues contentValues) {
            return contentResolver.insert(
                    insertQuery.uri(),
                    contentValues
            );
        }

        /**
         * {@inheritDoc}
         */
        @WorkerThread
        @Override
        public int update(@NonNull UpdateQuery updateQuery, @NonNull ContentValues contentValues) {
            return contentResolver.update(
                    updateQuery.uri(),
                    contentValues,
                    nullableString(updateQuery.where()),
                    nullableArrayOfStringsFromListOfStrings(updateQuery.whereArgs())
            );
        }

        /**
         * {@inheritDoc}
         */
        @WorkerThread
        @Override
        public int delete(@NonNull DeleteQuery deleteQuery) {
            return contentResolver.delete(
                    deleteQuery.uri(),
                    nullableString(deleteQuery.where()),
                    nullableArrayOfStringsFromListOfStrings(deleteQuery.whereArgs())
            );
        }

        /**
         * {@inheritDoc}
         */
        @NonNull
        @Override
        public ContentResolver contentResolver() {
            return contentResolver;
        }
    }
}

package com.pushtorefresh.storio3;

import android.os.SystemClock;
import android.util.Log;

import com.pushtorefresh.storio3.operations.PreparedOperation;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoggingInterceptor implements Interceptor {

    @SuppressWarnings("LogNotTimber")   // Library should not depend on Timber.
    @NonNull
    public static LoggingInterceptor defaultLogger() {
        return new LoggingInterceptor(new Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.d("StorIO", message);
            }
        });
    }

    @NonNull
    public static LoggingInterceptor withLogger(@NonNull Logger logger) {
        return new LoggingInterceptor(logger);
    }

    @NonNull
    private final Logger logger;

    private LoggingInterceptor(@NonNull Logger logger) {
        this.logger = logger;
    }

    @Nullable
    @Override
    public <Result, WrappedResult, Data> Result intercept(@NonNull PreparedOperation<Result, WrappedResult, Data> operation, @NonNull Chain chain) {
        final long startMillis = SystemClock.elapsedRealtime();

        final Result result = chain.proceed(operation);

        final long endMillis = SystemClock.elapsedRealtime();
        logger.log(
                String.format(
                        Locale.US,
                        "%s\n=> data: %s\n<= result: %s\ntook %dms",
                        operation.getClass().getSimpleName(),
                        operation.getData(),
                        result,
                        endMillis - startMillis
                )
        );

        return result;
    }

    public interface Logger {

        void log(@NonNull String message);
    }
}

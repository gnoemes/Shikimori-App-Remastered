package com.pushtorefresh.storio3.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Flowable;

/**
 * FOR INTERNAL USE ONLY.
 * <p>
 * Thread-safe changes bus.
 */
public final class ChangesBus<T> {

    @Nullable
    private final RxChangesBus<T> rxChangesBus;

    public ChangesBus(boolean rxJavaIsInTheClassPath) {
        rxChangesBus = rxJavaIsInTheClassPath
                ? new RxChangesBus<T>()
                : null;
    }

    public void onNext(@NonNull T next) {
        if (rxChangesBus != null) {
            rxChangesBus.onNext(next);
        }
    }

    @Nullable
    public Flowable<T> asFlowable() {
        return rxChangesBus != null
                ? rxChangesBus.asFlowable()
                : null;
    }
}

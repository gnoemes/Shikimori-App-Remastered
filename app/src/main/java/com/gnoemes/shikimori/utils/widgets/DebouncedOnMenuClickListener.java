package com.gnoemes.shikimori.utils.widgets;

import android.os.SystemClock;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A Debounced OnClickListener
 * Rejects clicks that are too close together in time.
 * This class is safe to use as an OnClickListener for multiple views, and will debounce each one separately.
 */
public abstract class DebouncedOnMenuClickListener implements Toolbar.OnMenuItemClickListener {

    private final long minimumInterval;
    private Map<MenuItem, Long> lastClickMap;

    /**
     * Implement this in your subclass instead of onClick
     * @param v The view that was clicked
     */
    public abstract Boolean onDebouncedClick(MenuItem v);

    /**
     * The one and only constructor
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnMenuClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<MenuItem, Long>();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Long previousClickTimestamp = lastClickMap.get(item);
        long currentTimestamp = SystemClock.uptimeMillis();

        lastClickMap.put(item, currentTimestamp);
        if(previousClickTimestamp == null || Math.abs(currentTimestamp - previousClickTimestamp) > minimumInterval) {
            return   onDebouncedClick(item);
        }
        return false;
    }
}
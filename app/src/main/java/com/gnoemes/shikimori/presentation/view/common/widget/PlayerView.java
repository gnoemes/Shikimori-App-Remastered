/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gnoemes.shikimori.presentation.view.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;

/**
 * Custom player view that extends ExoPlayer's {@link StyledPlayerView}.
 *
 * <p>This replaces the original forked ExoPlayer 2.9.6 PlayerView. The upstream
 * {@link StyledPlayerView} (ExoPlayer 2.19.1) provides all the same functionality
 * natively. This subclass adds only the custom {@link #setControllerShowOnTouch(boolean)}
 * method used by the app.
 */
public class PlayerView extends StyledPlayerView {

    private boolean controllerShowOnTouch = true;

    public PlayerView(Context context) {
        super(context);
    }

    public PlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets a {@link PlayerControlView.VisibilityListener} as the controller visibility listener.
     *
     * <p>This bridges the app's existing {@link PlayerControlView.VisibilityListener} usage
     * (interface method {@code onVisibilityChange(int)}) to the parent {@link StyledPlayerView}'s
     * {@link com.google.android.exoplayer2.ui.StyledPlayerView.ControllerVisibilityListener}
     * (interface method {@code onVisibilityChanged(int)}).
     */
    public void setControllerVisibilityListener(final PlayerControlView.VisibilityListener listener) {
        if (listener == null) {
            super.setControllerVisibilityListener(
                    (StyledPlayerView.ControllerVisibilityListener) null);
        } else {
            super.setControllerVisibilityListener(
                    (StyledPlayerView.ControllerVisibilityListener) visibility ->
                            listener.onVisibilityChange(visibility));
        }
    }

    /**
     * Sets whether tapping on the view shows the playback controller.
     *
     * <p>If {@code true} (the default), tapping the view when the controller is hidden
     * will show it (standard {@link StyledPlayerView} behavior).
     *
     * <p>If {@code false}, tapping the view will never show the controller; it can only
     * hide it if it was already visible. The app uses this in combination with a
     * custom gesture detector to manage controller visibility.
     *
     * @param controllerShowOnTouch Whether tapping the view should show the controller.
     */
    public void setControllerShowOnTouch(boolean controllerShowOnTouch) {
        this.controllerShowOnTouch = controllerShowOnTouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if (!controllerShowOnTouch) {
            // Only allow hiding, never showing on touch.
            // The app manages controller visibility via its own gesture detector.
            if (isControllerFullyVisible()) {
                hideController();
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }
}

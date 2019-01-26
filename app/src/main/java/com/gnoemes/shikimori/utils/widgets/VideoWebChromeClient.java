package com.gnoemes.shikimori.utils.widgets;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.gnoemes.shikimori.presentation.view.player.WebPlayerActivity;


public class VideoWebChromeClient extends WebChromeClient {
    private boolean isVideoFullscreen = false;
    private ViewGroup activityVideoView;
    private View videoViewContainer;
    private CustomViewCallback videoCallback;
    private WebPlayerActivity.WindowCallback windowCallback;

    public VideoWebChromeClient(ViewGroup activityVideoView, WebPlayerActivity.WindowCallback windowCallback) {
        this.activityVideoView = activityVideoView;
        this.windowCallback = windowCallback;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        Log.i("DEVE", "ENTER FULLSCREEN");
        videoCallback = callback;
        videoViewContainer = view;
        windowCallback.onFullscreenMode();
        activityVideoView.addView(videoViewContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        activityVideoView.setVisibility(View.VISIBLE);
        isVideoFullscreen = true;
    }

    @Override
    public void onHideCustomView() {
        if (!isVideoFullscreen) {
            return;
        }
        Log.i("DEVE", "EXIT FULLSCREEN");
        activityVideoView.setVisibility(View.VISIBLE);
        activityVideoView.removeView(videoViewContainer);
        windowCallback.onNormalMode();
        videoViewContainer = null;
        videoCallback.onCustomViewHidden();
        isVideoFullscreen = false;
    }

    public boolean onBackPressed() {
        onHideCustomView();
        return isVideoFullscreen;
    }

    @Override
    public void onCloseWindow(WebView window) {
        window.destroy();
        windowCallback = null;
        videoCallback = null;
        super.onCloseWindow(window);
    }
}
package com.gnoemes.shikimori.utils.widgets;

public class OverlapHeaderScrollingBehavior extends FixScrollingFooterBehavior {

    @Override
    protected boolean shouldHeaderOverlapScrollingChild() {
        return true;
    }
}

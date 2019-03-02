package com.gnoemes.shikimori.presentation.view.base.activity

import android.os.Bundle
import com.gnoemes.shikimori.R

abstract class BaseThemedActivity  : MvpActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.ShikimoriAppTheme_Dark)
        super.onCreate(savedInstanceState)
    }
}
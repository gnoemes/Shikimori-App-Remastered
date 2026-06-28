package com.gnoemes.shikimori

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.gnoemes.shikimori.di.app.component.DaggerAppComponent
import dagger.android.*
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DaggerAppComponent.builder().create(this).inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}

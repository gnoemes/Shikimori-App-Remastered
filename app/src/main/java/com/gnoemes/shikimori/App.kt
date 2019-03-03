package com.gnoemes.shikimori

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import androidx.appcompat.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.gnoemes.shikimori.di.app.component.DaggerAppComponent
import dagger.android.*
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

class App : Application(), HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var broadcastReceiverDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        JodaTimeAndroid.init(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DaggerAppComponent.builder().create(this).inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
    override fun serviceInjector(): AndroidInjector<Service> = serviceDispatchingAndroidInjector
    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverDispatchingAndroidInjector
}
package com.gnoemes.shikimori

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import androidx.appcompat.app.AppCompatDelegate
import com.gnoemes.shikimori.di.app.component.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.*
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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        JodaTimeAndroid.init(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        DaggerAppComponent.builder().create(this).inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
    override fun serviceInjector(): AndroidInjector<Service> = serviceDispatchingAndroidInjector
    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverDispatchingAndroidInjector
}
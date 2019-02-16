package io.debug

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import io.core.common.Logger
import io.debug.runtime.BuildConfig
import io.fabric.sdk.android.Fabric
import timber.log.Timber

object DebugInitializer {

    fun init(application: Application) {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(application)
            Timber.plant(Timber.DebugTree())
            Logger.addPrinter(LoggerPrinter.Timber())
        } else {
            Logger.addPrinter(LoggerPrinter.Crashlytics())
        }
    }

    fun initFabric(application: Application) {
        val crashlyticsCore = CrashlyticsCore.Builder()
            .disabled(BuildConfig.DEBUG)
            .build()
        Fabric.with(application, Crashlytics.Builder().core(crashlyticsCore).build())
    }

}
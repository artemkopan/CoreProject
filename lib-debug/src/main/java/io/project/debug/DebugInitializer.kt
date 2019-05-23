package io.project.debug

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.fabric.sdk.android.Fabric
import io.project.core.app.Logger
import okhttp3.Interceptor
import timber.log.Timber

object DebugInitializer {

    @JvmStatic
    fun initLoggers(application: Application) {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(application)
            Timber.plant(Timber.DebugTree())
            Logger.addPrinter(LoggerPrinter.Timber())
        } else {
            Logger.addPrinter(LoggerPrinter.Crashlytics())
        }
    }

    @JvmStatic
    fun initFabric(application: Application, isDisabled: Boolean) {
        val crashlyticsCore = CrashlyticsCore.Builder()
            .disabled(isDisabled)
            .build()
        Fabric.with(application, Crashlytics.Builder().core(crashlyticsCore).build())
    }

    @JvmStatic
    fun initDebugInterceptor(): Interceptor {
        return StethoInterceptor()
    }

}
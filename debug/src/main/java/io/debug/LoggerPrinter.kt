package io.debug

import android.util.Log
import io.core.common.Logger


typealias CrashlyticsLib = com.crashlytics.android.Crashlytics

internal interface LoggerPrinter {

    class Timber : Logger.Printer {

        override fun log(
            @Logger.Priority priority: Int, tag: String,
            message: String,
            t: Throwable?
        ) {
            when (priority) {
                Logger.DEBUG -> timber.log.Timber.tag(tag).log(Log.DEBUG, t, message)
                Logger.ERROR -> timber.log.Timber.tag(tag).log(Log.ERROR, t, message)
                Logger.INFO -> timber.log.Timber.tag(tag).log(Log.INFO, t, message)
                Logger.WARN -> timber.log.Timber.tag(tag).log(Log.WARN, t, message)
                else -> timber.log.Timber.tag(tag).log(priority, t, message)
            }
        }
    }

    class Crashlytics : Logger.Printer {
        override fun log(
            @Logger.Priority priority: Int, tag: String,
            message: String,
            t: Throwable?
        ) {
            when (priority) {
                Logger.DEBUG -> CrashlyticsLib.log(Log.DEBUG, tag, message)
                Logger.ERROR -> {
                    CrashlyticsLib.log(Log.ERROR, tag, message)
                    t?.let { CrashlyticsLib.logException(it) }
                }
                Logger.INFO -> CrashlyticsLib.log(Log.INFO, tag, message)
                Logger.WARN -> {
                    CrashlyticsLib.log(Log.WARN, tag, message)
                    t?.let { CrashlyticsLib.logException(it) }
                }
                else -> CrashlyticsLib.log(priority, tag, message)
            }
        }
    }

}

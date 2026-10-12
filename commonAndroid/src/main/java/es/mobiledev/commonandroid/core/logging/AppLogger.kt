package es.mobiledev.commonandroid.core.logging

import android.util.Log
import es.mobiledev.common.error.AppError

/**
 * Logger interface for reporting application errors.
 * Implementations can route logs to Logcat, Crashlytics, Sentry, or test spies.
 */
interface AppLogger {
    /**
     * Reports an application [error].
     *
     * @param tag Source identifier (e.g., ViewModel name).
     * @param error The failure to log.
     */
    fun logAppError(
        tag: String,
        error: AppError,
    )
}

/**
 * Default [AppLogger] implementation that writes to Logcat.
 */
object LogcatAppLogger : AppLogger {
    override fun logAppError(
        tag: String,
        error: AppError,
    ) {
        Log.e(tag, error.message, error.throwable)
    }
}

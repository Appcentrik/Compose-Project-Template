package es.mobiledev.commonandroid.core.logging

import android.util.Log
import es.mobiledev.common.error.AppError

/**
 * Default [AppLogger], writing to Logcat.
 *
 * Bound in the application DI module. Replace that binding to report elsewhere, keeping the tag so
 * the origin stays traceable.
 */
object LogcatAppLogger : AppLogger {
    override fun logAppError(
        tag: String,
        error: AppError,
    ) {
        Log.e(tag, error.message, error.throwable)
    }
}

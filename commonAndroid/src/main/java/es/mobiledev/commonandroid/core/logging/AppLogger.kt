package es.mobiledev.commonandroid.core.logging

import es.mobiledev.common.error.AppError

/**
 * Entry point for reporting failures.
 *
 * [es.mobiledev.commonandroid.ui.base.BaseViewModel] only depends on this interface, so replacing
 * Logcat with Crashlytics, Sentry or a test spy means providing another implementation in the DI
 * graph, without touching any ViewModel.
 */
interface AppLogger {
    /**
     * Reports [error].
     *
     * @param tag identifies the source, normally the ViewModel simple name.
     * @param error the failure, whose [AppError.message] is technical copy meant for logs only.
     */
    fun logAppError(
        tag: String,
        error: AppError,
    )
}

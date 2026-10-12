package es.mobiledev.data.local.article.error

import es.mobiledev.common.error.AppError
import es.mobiledev.common.extensions.messageOrTypeName
import es.mobiledev.common.response.AsyncResultException
import kotlinx.coroutines.CancellationException

/**
 * Runs [call] and translates any failure into an [AsyncResultException] carrying an [AppError.LocalError].
 *
 * Failures that are already mapped are forwarded untouched so that the original [AppError] is never
 * downgraded, and [CancellationException] is always rethrown to keep structured concurrency intact.
 */
suspend fun <T> catchLocalError(call: suspend () -> T): T =
    try {
        call()
    } catch (e: CancellationException) {
        throw e
    } catch (e: AsyncResultException) {
        throw e
    } catch (e: Exception) {
        throw AsyncResultException(
            AppError.LocalError(
                message = e.messageOrTypeName(),
                throwable = e
            )
        )
    }

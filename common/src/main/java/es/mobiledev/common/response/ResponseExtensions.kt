package es.mobiledev.common.response

import es.mobiledev.common.error.AppError
import es.mobiledev.common.extensions.messageOrTypeName
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Runs [block] once and wraps the outcome into an [AsyncResult], classifying unmapped failures as
 * [AppError.UnknownError].
 *
 * Use it for one shot calls. Failures that already carry an [AppError] are forwarded untouched, and
 * [CancellationException] is always rethrown to keep structured concurrency intact.
 */
suspend fun <T> remoteResult(block: suspend () -> T): AsyncResult<T> = asResult(block, AppError::UnknownError)

/**
 * Same as [remoteResult] but classifies unmapped failures as [AppError.LocalError].
 */
suspend fun <T> localResult(block: suspend () -> T): AsyncResult<T> = asResult(block, AppError::LocalError)

/**
 * Single shot version of [remoteResult] exposed as a [Flow], so reads and writes share the same
 * consumption pattern through [onResult].
 */
fun <T> remoteResultFlow(block: suspend () -> T): Flow<AsyncResult<T>> = flow { emit(asResult(block, AppError::UnknownError)) }

/**
 * Wraps a suspend [block] returning a [Flow]: every emitted value is forwarded as an
 * [AsyncResult.Success], and an upstream failure becomes a final [AsyncResult.Error] emission before
 * the flow completes.
 */
fun <T> localResultFlow(block: suspend () -> Flow<T>): Flow<AsyncResult<T>> =
    flow {
        val upstream =
            try {
                block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                emit(AsyncResult.Error(e.toAppError(AppError::LocalError)))
                return@flow
            }

        emitAll(
            upstream
                .map { AsyncResult.Success(it) as AsyncResult<T> }
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    emit(AsyncResult.Error(exception.toAppError(AppError::LocalError)))
                },
        )
    }

private suspend fun <T> asResult(
    block: suspend () -> T,
    toAppError: (message: String, throwable: Throwable) -> AppError,
): AsyncResult<T> =
    try {
        AsyncResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        AsyncResult.Error(e.toAppError(toAppError))
    }

private fun Throwable.toAppError(
    toAppError: (message: String, throwable: Throwable) -> AppError,
): AppError =
    if (this is AsyncResultException) {
        error
    } else {
        toAppError(messageOrTypeName(), this)
    }

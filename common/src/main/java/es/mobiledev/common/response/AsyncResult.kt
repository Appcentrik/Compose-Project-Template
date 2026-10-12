package es.mobiledev.common.response

import es.mobiledev.common.error.AppError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.transformWhile

/**
 * Represents the outcome of an operation: either [Success] with data or [Error] with an [AppError].
 */
sealed class AsyncResult<out T> {
    data class Success<T>(
        val data: T,
    ) : AsyncResult<T>()

    data class Error(
        val error: AppError,
    ) : AsyncResult<Nothing>()
}

/**
 * Transforms the value of an [AsyncResult.Success] with [transform].
 *
 * An [AsyncResult.Error] is returned unchanged and [transform] is not invoked.
 *
 * @param transform conversion applied to the success value.
 * @return a result holding the transformed value, or the original error.
 */
inline fun <T, R> AsyncResult<T>.map(transform: (T) -> R): AsyncResult<R> =
    when (this) {
        is AsyncResult.Success -> AsyncResult.Success(transform(data))
        is AsyncResult.Error -> this
    }

/**
 * Chains an operation that can fail on top of this result.
 *
 * When this is an [AsyncResult.Success], [transform] is invoked with its value and its result is
 * returned. When this is an [AsyncResult.Error], the error is returned unchanged and [transform]
 * is not invoked, so the first failure short-circuits the chain.
 *
 * @param transform operation to run with the success value; it may suspend.
 * @return the result produced by [transform], or the original error.
 */
suspend fun <T, R> AsyncResult<T>.flatMap(
    transform: suspend (T) -> AsyncResult<R>,
): AsyncResult<R> =
    when (this) {
        is AsyncResult.Success -> transform(data)
        is AsyncResult.Error -> this
    }

/**
 * Returns the value of an [AsyncResult.Success], or the value derived from the error.
 *
 * @param fallback computes the replacement value from the error.
 * @return the success value, or the result of [fallback].
 */
inline fun <T> AsyncResult<T>.getOrElse(fallback: (AppError) -> T): T =
    when (this) {
        is AsyncResult.Success -> data
        is AsyncResult.Error -> fallback(error)
    }

/**
 * Handles the result of an operation by executing the provided callbacks for success or error.
 */
suspend fun <T> AsyncResult<T>.onResult(
    onSuccess: suspend (data: T) -> Unit,
    onError: suspend (error: AppError) -> Unit,
) {
    when (this) {
        is AsyncResult.Success -> onSuccess(data)
        is AsyncResult.Error -> onError(error)
    }
}

/**
 * Collects a [Flow] of [AsyncResult], handling [AsyncResult.Success] and [AsyncResult.Error]
 * emissions.
 *
 * An error ends the collection: it is handed to [onError] and nothing after it is processed.
 */
suspend fun <T> Flow<AsyncResult<T>>.onResult(
    onSuccess: suspend (data: T) -> Unit,
    onError: suspend (error: AppError) -> Unit,
) {
    transformWhile { result ->
        emit(result)
        result is AsyncResult.Success
    }.collectLatest { result ->
        result.onResult(onSuccess = onSuccess, onError = onError)
    }
}

/**
 * Exception carrying an [AppError] within the data layer to prevent re-mapping.
 */
class AsyncResultException(
    val error: AppError
) : Exception()

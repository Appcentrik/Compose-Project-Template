package es.mobiledev.common.response

import es.mobiledev.common.error.AppError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Outcome of an operation, either a [Success] holding the data or an [Error] holding the already
 * mapped [AppError].
 *
 * Every gateway in this template returns it, reads and writes alike, so consumers never have to mix
 * the value, the [AsyncResult] and the thrown exception idioms.
 */
sealed class AsyncResult<out T> {
    data class Success<T>(
        val data: T
    ) : AsyncResult<T>()

    data class Error(
        val error: AppError
    ) : AsyncResult<Nothing>()
}

/**
 * Terminal consumer of a [Flow] of [AsyncResult]. Both callbacks are mutually exclusive per
 * emission.
 */
suspend fun <T> Flow<AsyncResult<T>>.onResult(
    onSuccess: suspend (data: T) -> Unit,
    onError: suspend (error: AppError) -> Unit,
) {
    collectLatest { result ->
        when (result) {
            is AsyncResult.Success -> onSuccess(result.data)
            is AsyncResult.Error -> onError(result.error)
        }
    }
}

/**
 * Carries an [AppError] across the throwing boundary of the data sources, so a failure already
 * mapped down there is not downgraded on its way up.
 *
 * It must never escape a gateway: every gateway returns [AsyncResult], so the exception only travels
 * inside the data layer, from `catchRemoteError`/`catchLocalError` up to the repository.
 */
class AsyncResultException(
    val error: AppError
) : Exception()

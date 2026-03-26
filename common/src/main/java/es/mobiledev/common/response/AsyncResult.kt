package es.mobiledev.common.response

import es.mobiledev.common.error.AppError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

sealed class AsyncResult<out T> {
    data class Success<T>(
        val data: T
    ) : AsyncResult<T>()

    data class Error(
        val error: AppError
    ) : AsyncResult<Nothing>()
}

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

class AsyncResultException(
    val error: AppError
) : Exception()

class AsyncResultScope {
    suspend fun <T> remoteResponse(call: suspend () -> AsyncResult<T>): T =
        when (val result = call()) {
            is AsyncResult.Success -> result.data
            is AsyncResult.Error -> throw AsyncResultException(result.error)
        }

    suspend fun <T> localResponse(call: suspend () -> AsyncResult<T>): T =
        when (val result = call()) {
            is AsyncResult.Success -> result.data
            is AsyncResult.Error -> throw AsyncResultException(result.error)
        }
}

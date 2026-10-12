package es.mobiledev.data.remote.error

import es.mobiledev.common.error.AppError
import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.response.AsyncResultException
import es.mobiledev.common.util.AppDispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Helper to execute remote API calls and wrap results into cold [Flow] with error handling.
 */
class RemoteResponse<T>(
    private val appDispatchers: AppDispatchers,
    private val block: suspend () -> T,
) {
    fun flow(): Flow<AsyncResult<T>> = flow { emit(execute()) }.flowOn(appDispatchers.io)

    private suspend fun execute(): AsyncResult<T> =
        try {
            AsyncResult.Success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            AsyncResult.Error(e.toAppError())
        }
}

fun <T> remoteResponse(
    appDispatchers: AppDispatchers,
    block: suspend () -> T,
): RemoteResponse<T> = RemoteResponse(appDispatchers, block)

/** Maps throwables to remote [AppError] or preserves existing [AsyncResultException]. */
private fun Throwable.toAppError(): AppError =
    if (this is AsyncResultException) {
        error
    } else {
        manageException(this)
    }

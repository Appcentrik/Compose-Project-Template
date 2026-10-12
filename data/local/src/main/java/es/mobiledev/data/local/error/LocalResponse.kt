package es.mobiledev.data.local.error

import es.mobiledev.common.error.AppError
import es.mobiledev.common.extensions.messageOrTypeName
import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.response.AsyncResultException
import es.mobiledev.common.util.AppDispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Template for a local call: runs [block] once and hands the outcome over as a value or as a [Flow].
 *
 * The local counterpart of the remote template, so both sides share one shape. They stay separate
 * because they classify failures differently: everything unmapped here is a storage problem, while
 * the remote side has to tell a timeout apart from a lost connection.
 *
 * Both access modes are used: reads ask for [flow], writes for [value]. A write is a single
 * operation, not a stream, so wrapping it in a flow would only force the caller to take the first
 * emission.
 *
 * The [Flow] is returned cold on purpose. Collecting it runs the call again, which is what a retry
 * needs, so nothing has to be cached or invalidated.
 */
class LocalResponse<T>(
    private val appDispatchers: AppDispatchers,
    private val block: suspend () -> T,
) {
    suspend fun value(): AsyncResult<T> = execute()

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

fun <T> localResponse(
    appDispatchers: AppDispatchers,
    block: suspend () -> T,
): LocalResponse<T> = LocalResponse(appDispatchers, block)

/** An already mapped failure keeps its cause; anything else is reported as a local failure. */
private fun Throwable.toAppError(): AppError =
    if (this is AsyncResultException) {
        error
    } else {
        AppError.LocalError(message = messageOrTypeName(), throwable = this)
    }

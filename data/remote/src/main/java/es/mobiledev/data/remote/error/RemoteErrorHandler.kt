package es.mobiledev.data.remote.error

import com.squareup.moshi.JsonDataException
import es.mobiledev.common.error.AppError
import es.mobiledev.common.extensions.messageOrTypeName
import es.mobiledev.common.response.AsyncResultException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Runs [call] and translates any failure into an [AsyncResultException] carrying a mapped [AppError].
 *
 * Failures that are already mapped are forwarded untouched so that the original [AppError] is never
 * downgraded to [AppError.UnknownError], and [CancellationException] is always rethrown to keep
 * structured concurrency intact.
 */
suspend fun <T> catchRemoteError(call: suspend () -> T): T =
    try {
        call()
    } catch (e: CancellationException) {
        throw e
    } catch (e: AsyncResultException) {
        throw e
    } catch (e: Exception) {
        throw AsyncResultException(manageException(e))
    }

private fun manageException(cause: Throwable) =
    when (cause) {
        is AsyncResultException -> cause.error

        // Checked before IOException: it is a subclass of it, and the copy must say "took too long"
        // instead of "no internet connection".
        is SocketTimeoutException ->
            AppError.TimeoutError(
                message = cause.messageOrTypeName(),
                throwable = cause
            )

        is HttpException ->
            when (cause.code()) {
                HTTP_UNAUTHORIZED, HTTP_FORBIDDEN ->
                    AppError.UnauthorizedError(
                        message = cause.messageOrTypeName(),
                        throwable = cause
                    )

                HTTP_NOT_FOUND ->
                    AppError.NotFoundError(
                        message = cause.messageOrTypeName(),
                        throwable = cause
                    )

                else ->
                    AppError.ServerError(
                        code = cause.code(),
                        message = cause.messageOrTypeName(),
                        throwable = cause
                    )
            }

        is IOException ->
            AppError.NetworkError(
                message = cause.messageOrTypeName(),
                throwable = cause
            )

        is JsonDataException ->
            AppError.ParseError(
                message = cause.messageOrTypeName(),
                throwable = cause
            )

        else ->
            AppError.UnknownError(
                message = cause.messageOrTypeName(),
                throwable = cause
            )
    }

private const val HTTP_UNAUTHORIZED = 401
private const val HTTP_FORBIDDEN = 403
private const val HTTP_NOT_FOUND = 404

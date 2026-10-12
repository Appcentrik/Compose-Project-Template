package es.mobiledev.data.remote.error

import com.squareup.moshi.JsonDataException
import es.mobiledev.common.error.AppError
import es.mobiledev.common.extensions.messageOrTypeName
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.io.IOException

/** Maps a failure raised while talking to the server onto the matching [AppError]. */
fun manageException(cause: Throwable): AppError =
    when (cause) {
        is InterruptedIOException ->
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

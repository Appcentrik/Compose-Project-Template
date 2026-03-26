package es.mobiledev.data.remote.error

import com.squareup.moshi.JsonDataException
import es.mobiledev.common.error.AppError
import es.mobiledev.common.response.AsyncResultException
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> catchRemoteError(call: suspend () -> T): T =
    try {
        call()
    } catch (e: Exception) {
        throw AsyncResultException(manageException(e))
    }

private fun manageException(cause: Throwable) =
    when (cause) {
        is HttpException ->
            AppError.ServerError(
                code = cause.code(),
                message = cause.message(),
                throwable = cause
            )

        is IOException ->
            AppError.NetworkError(
                message = cause.message ?: "IOException",
                throwable = cause
            )

        is JsonDataException ->
            AppError.ParseError(
                message = cause.message ?: "JsonDataException",
                throwable = cause
            )
        else ->
            AppError.UnknownError(
                message = cause.message ?: "UnknownError",
                throwable = cause
            )
    }

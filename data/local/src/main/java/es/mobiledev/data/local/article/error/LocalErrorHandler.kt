package es.mobiledev.data.local.article.error

import es.mobiledev.common.EMPTY_STRING
import es.mobiledev.common.error.AppError
import es.mobiledev.common.response.AsyncResultException

suspend fun <T> catchLocalError(call: suspend () -> T): T =
    try {
        call()
    } catch (e: Exception) {
        throw AsyncResultException(
            AppError.LocalError(
                message = e.message ?: EMPTY_STRING,
                throwable = e
            )
        )
    }

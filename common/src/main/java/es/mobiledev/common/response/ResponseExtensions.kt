package es.mobiledev.common.response

import es.mobiledev.common.EMPTY_STRING
import es.mobiledev.common.error.AppError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <T> remoteResponse(
    block: suspend () -> T,
): Flow<AsyncResult<T>> =
    flow {
        emit(
            try {
                AsyncResult.Success(block())
            } catch (e: CancellationException) {
                throw e
            } catch (e: AsyncResultException) {
                AsyncResult.Error(e.error)
            } catch (e: Exception) {
                AsyncResult.Error(
                    AppError.UnknownError(
                        message = e.message ?: EMPTY_STRING,
                        throwable = e,
                    ),
                )
            }
        )
    }

fun <T> localResponse(
    block: suspend () -> T,
): Flow<AsyncResult<T>> =
    flow {
        emit(
            try {
                AsyncResult.Success(block())
            } catch (e: CancellationException) {
                throw e
            } catch (e: AsyncResultException) {
                AsyncResult.Error(e.error)
            } catch (e: Exception) {
                AsyncResult.Error(
                    AppError.LocalError(
                        message = e.message ?: EMPTY_STRING,
                        throwable = e,
                    ),
                )
            }
        )
    }

fun <T> localResponseFlow(
    block: suspend () -> Flow<T>,
): Flow<AsyncResult<T>> =
    flow {
        try {
            emitAll(
                block()
                    .map { AsyncResult.Success(it) as AsyncResult<T> }
                    .catch { exception ->
                        if (exception is CancellationException) throw exception
                        emit(
                            AsyncResult.Error(
                                if (exception is AsyncResultException) {
                                    exception.error
                                } else {
                                    AppError.LocalError(
                                        message = exception.message ?: EMPTY_STRING,
                                        throwable = exception,
                                    )
                                }
                            )
                        )
                    }
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: AsyncResultException) {
            emit(AsyncResult.Error(e.error))
        } catch (e: Exception) {
            emit(
                AsyncResult.Error(
                    AppError.LocalError(
                        message = e.message ?: EMPTY_STRING,
                        throwable = e,
                    ),
                ),
            )
        }
    }

fun <T> localRemoteResponse(
    block: suspend AsyncResultScope.() -> T,
): Flow<AsyncResult<T>> =
    flow {
        emit(
            try {
                AsyncResult.Success(AsyncResultScope().block())
            } catch (e: CancellationException) {
                throw e
            } catch (e: AsyncResultException) {
                AsyncResult.Error(e.error)
            } catch (e: Exception) {
                AsyncResult.Error(
                    AppError.UnknownError(
                        message = e.message ?: EMPTY_STRING,
                        throwable = e,
                    ),
                )
            }
        )
    }

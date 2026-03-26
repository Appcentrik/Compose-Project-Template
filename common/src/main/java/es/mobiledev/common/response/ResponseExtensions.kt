package es.mobiledev.common.response

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> remoteResponse(
    block: suspend () -> T,
): Flow<AsyncResult<T>> =
    flow {
        emit(
            try {
                AsyncResult.Success(block())
            } catch (e: AsyncResultException) {
                AsyncResult.Error(e.error)
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
            } catch (e: AsyncResultException) {
                AsyncResult.Error(e.error)
            }
        )
    }

fun <T> localRemoteResponse(
    block: suspend AsyncResultScope.() -> T,
): Flow<AsyncResult<T>> =
    flow {
        emit(
            try {
                AsyncResult.Success(AsyncResultScope().block())
            } catch (e: AsyncResultException) {
                AsyncResult.Error(e.error)
            }
        )
    }

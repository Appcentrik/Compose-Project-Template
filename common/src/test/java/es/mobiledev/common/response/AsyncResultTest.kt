package es.mobiledev.common.response

import es.mobiledev.common.error.AppError
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class AsyncResultTest {
    private val error = AppError.NetworkError("offline")
    private val failure: AsyncResult<Int> = AsyncResult.Error(error)

    @Test
    fun `map transforms the success value`() {
        assertEquals(AsyncResult.Success(6), AsyncResult.Success(2).map { it * 3 })
    }

    @Test
    fun `map leaves the error untouched and never runs the transform`() {
        var calls = 0

        val result =
            failure.map {
                calls++
                it
            }

        assertSame(error, (result as AsyncResult.Error).error)
        assertEquals(0, calls)
    }

    @Test
    fun `flatMap chains two successes`() =
        runTest {
            val result = AsyncResult.Success(2).flatMap { AsyncResult.Success(it * 3) }

            assertEquals(AsyncResult.Success(6), result)
        }

    @Test
    fun `flatMap stops at the first error without chaining`() =
        runTest {
            var calls = 0

            val result =
                failure.flatMap {
                    calls++
                    AsyncResult.Success(it)
                }

            assertSame(error, (result as AsyncResult.Error).error)
            assertEquals(0, calls)
        }

    @Test
    fun `getOrElse returns the value on success`() {
        assertEquals(2, AsyncResult.Success(2).getOrElse { -1 })
    }

    @Test
    fun `getOrElse applies the fallback with the error on failure`() {
        val value = failure.getOrElse { received -> if (received == error) -1 else 0 }

        assertEquals(-1, value)
    }

    @Test
    fun `onResult runs onSuccess with the data on success`() =
        runTest {
            var received: Int? = null
            var errors = 0

            AsyncResult.Success(2).onResult(
                onSuccess = { received = it },
                onError = { errors++ },
            )

            assertEquals(2, received)
            assertEquals(0, errors)
        }

    @Test
    fun `onResult runs onError with the error on failure`() =
        runTest {
            var successes = 0
            var received: AppError? = null

            failure.onResult(
                onSuccess = { successes++ },
                onError = { received = it },
            )

            assertSame(error, received)
            assertEquals(0, successes)
        }

    @Test
    fun `flow onResult handles every emission in order`() =
        runTest {
            val successes = mutableListOf<Int>()
            val errors = mutableListOf<AppError>()

            flowOf(
                AsyncResult.Success(1),
                AsyncResult.Success(2),
                AsyncResult.Error(error),
            ).onResult(
                onSuccess = { successes += it },
                onError = { errors += it },
            )

            assertEquals(listOf(1, 2), successes)
            assertEquals(listOf(error), errors)
        }

    @Test
    fun `flow onResult stops after an error`() =
        runTest {
            val successes = mutableListOf<Int>()
            val errors = mutableListOf<AppError>()

            flowOf(
                AsyncResult.Error(error),
                AsyncResult.Success(1),
            ).onResult(
                onSuccess = { successes += it },
                onError = { errors += it },
            )

            assertEquals(emptyList<Int>(), successes)
            assertEquals(listOf(error), errors)
        }
}

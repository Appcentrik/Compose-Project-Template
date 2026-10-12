package es.mobiledev.data.local.error

import es.mobiledev.common.error.AppError
import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.response.AsyncResultException
import es.mobiledev.common.util.AppDispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalResponseTest {
    private val dispatchers =
        AppDispatchers(
            main = Dispatchers.Unconfined,
            default = Dispatchers.Unconfined,
            io = Dispatchers.Unconfined,
        )

    @Test
    fun `wraps the returned value in a Success`() =
        runTest {
            val result = localResponse(dispatchers) { 7 }.value()

            assertEquals(AsyncResult.Success(7), result)
        }

    @Test
    fun `maps a thrown failure to LocalError`() =
        runTest {
            val result = localResponse(dispatchers) { throw IllegalStateException("disk full") }.value()

            assertTrue((result as AsyncResult.Error).error is AppError.LocalError)
        }

    @Test
    fun `keeps the original AppError when the call already mapped it`() =
        runTest {
            val original = AppError.NotFoundError(message = "gone")
            val result = localResponse(dispatchers) { throw AsyncResultException(original) }.value()

            assertEquals(original, (result as AsyncResult.Error).error)
        }

    @Test(expected = CancellationException::class)
    fun `rethrows CancellationException instead of reporting it as an error`() {
        runTest { localResponse(dispatchers) { throw CancellationException("cancelled") }.value() }
    }
}
